package rca.rw.secure.impls;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rca.rw.secure.dtos.plate.CreatePlateNumberDTO;
import rca.rw.secure.dtos.plate.PlateNumberResponseDTO;
import rca.rw.secure.dtos.plate.UpdatePlateNumberDTO;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.enums.vehicle.EPlateStatus;
import rca.rw.secure.models.PlateNumber;
import rca.rw.secure.models.VehicleOwner;
import rca.rw.secure.repos.IPlateNumberRepo;
import rca.rw.secure.repos.IVehicleOwnerRepo;
import rca.rw.secure.services.AuditLogService;
import rca.rw.secure.services.MailService;
import rca.rw.secure.services.PlateNumberService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlateNumberServiceImpl implements PlateNumberService {
    private final IPlateNumberRepo plateNumberRepository;
    private final IVehicleOwnerRepo vehicleOwnerRepo;
    private final AuditLogService auditLogService;
    private final MailService mailService;
    private static final Logger logger = LoggerFactory.getLogger(PlateNumberServiceImpl.class);

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> registerPlateNumber(CreatePlateNumberDTO request, String username) {
        if (plateNumberRepository.existsByPlateNumber(request.getPlateNumber())) {
            return ApiResponse.error("Plate number already exists: " + request.getPlateNumber(), HttpStatus.BAD_REQUEST, null);
        }

        VehicleOwner owner = vehicleOwnerRepo.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found with id: " + request.getOwnerId()));

        PlateNumber plateNumber = new PlateNumber();
        plateNumber.setPlateNumber(request.getPlateNumber());
        plateNumber.setVehicleOwner(owner);
        plateNumber.setStatus(EPlateStatus.AVAILABLE);

        PlateNumber savedPlateNumber = plateNumberRepository.save(plateNumber);

//        auditLogService.logAction("PlateNumber", savedPlateNumber.getId().toString(), "CREATE", username,
//                "Created plate: " + savedPlateNumber.getPlateNumber());
//        mailService.sendPlateCreatedEmail(username, savedPlateNumber);
        logger.info("Plate number created: {}", savedPlateNumber.getId());

        return ApiResponse.success("Plate number created successfully", HttpStatus.CREATED, mapToResponse(savedPlateNumber));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> updatePlateNumber(Long id, UpdatePlateNumberDTO request, String username) {
        PlateNumber plateNumber = plateNumberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plate number not found with id: " + id));

        if (request.getPlateNumber() != null && !request.getPlateNumber().equals(plateNumber.getPlateNumber())) {
            if (plateNumberRepository.existsByPlateNumber(request.getPlateNumber())) {
                return ApiResponse.error("Plate number already exists: " + request.getPlateNumber(), HttpStatus.BAD_REQUEST, null);
            }
            plateNumber.setPlateNumber(request.getPlateNumber());
        }

        if (request.getOwnerId() != null) {
            VehicleOwner owner = vehicleOwnerRepo.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Vehicle owner not found with id: " + request.getOwnerId()));
            plateNumber.setVehicleOwner(owner);
        }

        if (request.getStatus() != null) {
            plateNumber.setStatus(request.getStatus());
        }

        PlateNumber updatedPlateNumber = plateNumberRepository.save(plateNumber);
        auditLogService.logAction("PlateNumber", id.toString(), "UPDATE", username,
                "Updated plate: " + updatedPlateNumber.getPlateNumber());
//        mailService.sendPlateUpdatedEmail(username, updatedPlateNumber);
        logger.info("Plate number updated: {}", id);

        return ApiResponse.success("Plate number updated successfully", HttpStatus.OK, mapToResponse(updatedPlateNumber));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePlateNumber(Long id, String username) {
        PlateNumber plateNumber = plateNumberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plate number not found with id: " + id));

        if (plateNumber.getStatus() == EPlateStatus.IN_USE) {
            return ApiResponse.error("Cannot delete a plate number that is currently in use", HttpStatus.BAD_REQUEST, null);
        }

        plateNumberRepository.delete(plateNumber);
        auditLogService.logAction("PlateNumber", id.toString(), "DELETE", username,
                "Deleted plate: " + plateNumber.getPlateNumber());
//        mailService.sendPlateDeletedEmail(username, plateNumber);
        logger.info("Plate number deleted: {}", id);

        return ApiResponse.success("Plate number deleted successfully", HttpStatus.OK, null);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<PlateNumberResponseDTO>>> getPlateNumbersByOwner(Long ownerId, Pageable pageable) {
        VehicleOwner owner = vehicleOwnerRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found with id: " + ownerId));

        Page<PlateNumber> plateNumbers = plateNumberRepository.findByVehicleOwner(owner, pageable);
        Page<PlateNumberResponseDTO> response = plateNumbers.map(this::mapToResponse);

        return ApiResponse.success("Plate numbers retrieved successfully", HttpStatus.OK, response);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<List<PlateNumberResponseDTO>>> getAvailablePlateNumbersByOwner(Long ownerId) {
        VehicleOwner owner = vehicleOwnerRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found with id: " + ownerId));

        List<PlateNumber> availablePlateNumbers = plateNumberRepository.findByVehicleOwnerAndStatus(owner, EPlateStatus.AVAILABLE);
        List<PlateNumberResponseDTO> response = availablePlateNumbers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ApiResponse.success("Available plate numbers retrieved successfully", HttpStatus.OK, response);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> getPlateNumberById(Long id) {
        PlateNumber plateNumber = plateNumberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plate number not found with id: " + id));
        return ApiResponse.success("Plate number retrieved successfully", HttpStatus.OK, mapToResponse(plateNumber));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> getPlateNumberByPlateNumber(String plateNumberStr) {
        PlateNumber plateNumber = plateNumberRepository.findByPlateNumber(plateNumberStr)
                .orElseThrow(() -> new RuntimeException("Plate number not found: " + plateNumberStr));
        return ApiResponse.success("Plate number retrieved successfully", HttpStatus.OK, mapToResponse(plateNumber));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<PlateNumberResponseDTO>>> getAllPlateNumbers(Pageable pageable) {
        Page<PlateNumber> plateNumbers = plateNumberRepository.findAll(pageable);
        Page<PlateNumberResponseDTO> response = plateNumbers.map(this::mapToResponse);
        return ApiResponse.success("Plate numbers retrieved successfully", HttpStatus.OK, response);
    }

    private PlateNumberResponseDTO mapToResponse(PlateNumber plateNumber) {
        return new PlateNumberResponseDTO(
                plateNumber.getId(),
                plateNumber.getPlateNumber(),
                plateNumber.getCreatedAt(),
                plateNumber.getVehicleOwner().getId(),
                plateNumber.getVehicleOwner().getOwnerNames(),
                plateNumber.getStatus()
        );
    }
}