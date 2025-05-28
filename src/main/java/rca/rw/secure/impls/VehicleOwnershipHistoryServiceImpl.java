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
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.vehicleOwnerHistory.CreateVehicleOwnershipHistoryDTO;
import rca.rw.secure.dtos.vehicleOwnerHistory.UpdateVehicleOwnershipHistoryDTO;
import rca.rw.secure.dtos.vehicleOwnerHistory.VehicleOwnershipHistoryResponseDTO;
import rca.rw.secure.exceptions.NotFoundException;
import rca.rw.secure.models.Vehicle;
import rca.rw.secure.models.VehicleOwner;
import rca.rw.secure.models.VehicleOwnershipHistory;
import rca.rw.secure.repos.IVehicleOwnershipHistoryRepo;
import rca.rw.secure.repos.IVehicleOwnerRepo;
import rca.rw.secure.repos.IVehicleRepo;
import rca.rw.secure.services.AuditLogService;
import rca.rw.secure.services.MailService;
import rca.rw.secure.services.VehicleOwnershipHistoryService;

@Service
@RequiredArgsConstructor
public class VehicleOwnershipHistoryServiceImpl implements VehicleOwnershipHistoryService {
    private final IVehicleOwnershipHistoryRepo ownershipHistoryRepo;
    private final IVehicleRepo vehicleRepo;
    private final IVehicleOwnerRepo vehicleOwnerRepo;
    private final AuditLogService auditLogService;
    private final MailService mailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleOwnershipHistoryServiceImpl.class);

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VehicleOwnershipHistoryResponseDTO>> createOwnershipHistory(CreateVehicleOwnershipHistoryDTO request, String username) {
        Vehicle vehicle = vehicleRepo.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + request.getVehicleId()));

        VehicleOwner owner = vehicleOwnerRepo.findById(request.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner not found with id: " + request.getOwnerId()));

        VehicleOwnershipHistory history = new VehicleOwnershipHistory();
        history.setVehicle(vehicle);
        history.setOwner(owner);
        history.setStartDate(request.getStartDate());
        history.setPurchasePrice(request.getPurchasePrice());
        history.setPreviousPlateNumber(request.getPreviousPlateNumber());
        history.setComments(request.getComments());

        VehicleOwnershipHistory savedHistory = ownershipHistoryRepo.save(history);

        auditLogService.logAction("VehicleOwnershipHistory", savedHistory.getId().toString(), "CREATE", username,
                "Created ownership history for vehicle: " + vehicle.getChassisNumber());
        // mailService.sendOwnershipHistoryCreatedEmail(username, savedHistory);
        LOGGER.info("Ownership history created: {}", savedHistory.getId());

        return ApiResponse.success("Ownership history created successfully", HttpStatus.CREATED, mapToResponse(savedHistory));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VehicleOwnershipHistoryResponseDTO>> updateOwnershipHistory(Long id, UpdateVehicleOwnershipHistoryDTO request, String username) {
        VehicleOwnershipHistory history = ownershipHistoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Ownership history not found with id: " + id));

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepo.findById(request.getVehicleId())
                    .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + request.getVehicleId()));
            history.setVehicle(vehicle);
        }

        if (request.getOwnerId() != null) {
            VehicleOwner owner = vehicleOwnerRepo.findById(request.getOwnerId())
                    .orElseThrow(() -> new NotFoundException("Owner not found with id: " + request.getOwnerId()));
            history.setOwner(owner);
        }

        if (request.getStartDate() != null) {
            history.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            history.setEndDate(request.getEndDate());
        }
        if (request.getPurchasePrice() != null) {
            history.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getPreviousPlateNumber() != null) {
            history.setPreviousPlateNumber(request.getPreviousPlateNumber());
        }
        if (request.getComments() != null) {
            history.setComments(request.getComments());
        }

        VehicleOwnershipHistory updatedHistory = ownershipHistoryRepo.save(history);
        auditLogService.logAction("VehicleOwnershipHistory", id.toString(), "UPDATE", username,
                "Updated ownership history for vehicle: " + updatedHistory.getVehicle().getChassisNumber());
        // mailService.sendOwnershipHistoryUpdatedEmail(username, updatedHistory);
        LOGGER.info("Ownership history updated: {}", id);

        return ApiResponse.success("Ownership history updated successfully", HttpStatus.OK, mapToResponse(updatedHistory));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOwnershipHistory(Long id, String username) {
        VehicleOwnershipHistory history = ownershipHistoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Ownership history not found with id: " + id));

        ownershipHistoryRepo.delete(history);
        auditLogService.logAction("VehicleOwnershipHistory", id.toString(), "DELETE", username,
                "Deleted ownership history for vehicle: " + history.getVehicle().getChassisNumber());
        // mailService.sendOwnershipHistoryDeletedEmail(username, history);
        LOGGER.info("Ownership history deleted: {}", id);

        return ApiResponse.success("Ownership history deleted successfully", HttpStatus.OK, null);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByVehicleId(Long vehicleId, Pageable pageable) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + vehicleId));

        Page<VehicleOwnershipHistory> history = ownershipHistoryRepo.findByVehicle(vehicle, pageable);
        Page<VehicleOwnershipHistoryResponseDTO> response = history.map(this::mapToResponse);

        return ApiResponse.success("Ownership history retrieved successfully", HttpStatus.OK, response);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByChassisNumber(String chassisNumber, Pageable pageable) {
        Page<VehicleOwnershipHistory> history = ownershipHistoryRepo.findByVehicleChassisNumber(chassisNumber, pageable);
        Page<VehicleOwnershipHistoryResponseDTO> response = history.map(this::mapToResponse);

        return ApiResponse.success("Ownership history retrieved successfully", HttpStatus.OK, response);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByPlateNumber(String plateNumber, Pageable pageable) {
        Page<VehicleOwnershipHistory> history = ownershipHistoryRepo.findByVehiclePlateNumber(plateNumber, pageable);
        Page<VehicleOwnershipHistoryResponseDTO> response = history.map(this::mapToResponse);

        return ApiResponse.success("Ownership history retrieved successfully", HttpStatus.OK, response);
    }

    private VehicleOwnershipHistoryResponseDTO mapToResponse(VehicleOwnershipHistory history) {
        return new VehicleOwnershipHistoryResponseDTO(
                history.getId(),
                history.getVehicle().getId(),
                history.getVehicle().getChassisNumber(),
                history.getOwner().getId(),
                history.getOwner().getOwnerNames(),
                history.getOwner().getNationalId(),
                history.getStartDate(),
                history.getEndDate(),
                history.getPurchasePrice(),
                history.getPreviousPlateNumber(),
                history.getComments()
        );
    }
}