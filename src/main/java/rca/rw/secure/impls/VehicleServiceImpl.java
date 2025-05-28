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
import rca.rw.secure.dtos.vehicle.CreateVehicleDTO;
import rca.rw.secure.dtos.vehicle.UpdateVehicleDTO;
import rca.rw.secure.dtos.vehicle.VehicleResponseDTO;
import rca.rw.secure.dtos.vehicle.VehicleTransferDTO;
import rca.rw.secure.enums.vehicle.EPlateStatus;
import rca.rw.secure.exceptions.BadRequestException;
import rca.rw.secure.exceptions.NotFoundException;
import rca.rw.secure.models.Vehicle;
import rca.rw.secure.models.VehicleOwner;
import rca.rw.secure.models.PlateNumber;
import rca.rw.secure.models.VehicleOwnershipHistory;
import rca.rw.secure.repos.IVehicleOwnerRepo;
import rca.rw.secure.repos.IPlateNumberRepo;
import rca.rw.secure.repos.IVehicleOwnershipHistoryRepo;
import rca.rw.secure.repos.IVehicleRepo;
import rca.rw.secure.services.AuditLogService;
import rca.rw.secure.services.MailService;
import rca.rw.secure.services.VehicleService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final IVehicleRepo vehicleRepo;
    private final IVehicleOwnerRepo vehicleOwnerRepo;
    private final IPlateNumberRepo plateNumberRepo;
    private final IVehicleOwnershipHistoryRepo ownershipHistoryRepo;
    private final AuditLogService auditLogService;
    private final MailService mailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleServiceImpl.class);

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> registerVehicle(CreateVehicleDTO request, String username) {
        if (vehicleRepo.existsByChassisNumber(request.getChassisNumber())) {
            throw new BadRequestException("Vehicle with chassis number already exists: " + request.getChassisNumber());
        }

        VehicleOwner owner = vehicleOwnerRepo.findById(request.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Vehicle owner not found with id: " + request.getOwnerId()));

        PlateNumber plateNumber = plateNumberRepo.findByPlateNumber(request.getPlateNumber())
                .orElseThrow(() -> new NotFoundException("Plate number not found: " + request.getPlateNumber()));

        if (!plateNumber.getVehicleOwner().getId().equals(owner.getId())) {
            throw new BadRequestException("Plate number does not belong to this owner");
        }

        if (plateNumber.getStatus() != EPlateStatus.AVAILABLE) {
            throw new BadRequestException("Plate number is already in use");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setChassisNumber(request.getChassisNumber());
        vehicle.setManufactureCompany(request.getManufactureCompany());
        vehicle.setManufactureYear(request.getManufactureYear());
        vehicle.setPrice(request.getPrice());
        vehicle.setModelName(request.getModelName());
        vehicle.setCurrentOwner(owner);
        vehicle.setPlateNumber(plateNumber);

        Vehicle savedVehicle = vehicleRepo.save(vehicle);

        plateNumber.setStatus(EPlateStatus.IN_USE);
        plateNumberRepo.save(plateNumber);

        VehicleOwnershipHistory ownershipHistory = new VehicleOwnershipHistory();
        ownershipHistory.setVehicle(savedVehicle);
        ownershipHistory.setOwner(owner);
        ownershipHistory.setStartDate(LocalDateTime.now());
        ownershipHistory.setPurchasePrice(request.getPrice());
        ownershipHistory.setComments("Initial registration");
        ownershipHistoryRepo.save(ownershipHistory);

//        auditLogService.logAction("Vehicle", savedVehicle.getId().toString(), "CREATE", username,
//                "Registered vehicle: " + savedVehicle.getChassisNumber());
        // mailService.sendVehicleRegisteredEmail(username, savedVehicle);
        LOGGER.info("Vehicle registered: {}", savedVehicle.getId());

        return ApiResponse.success("Vehicle registered successfully", HttpStatus.CREATED, mapToResponse(savedVehicle));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> updateVehicle(Long id, UpdateVehicleDTO request, String username) {
        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + id));

        if (request.getChassisNumber() != null && !vehicle.getChassisNumber().equals(request.getChassisNumber())) {
            if (vehicleRepo.existsByChassisNumber(request.getChassisNumber())) {
                throw new BadRequestException("Vehicle with chassis number already exists: " + request.getChassisNumber());
            }
            vehicle.setChassisNumber(request.getChassisNumber());
        }

        if (request.getManufactureCompany() != null) {
            vehicle.setManufactureCompany(request.getManufactureCompany());
        }
        if (request.getManufactureYear() != null) {
            vehicle.setManufactureYear(request.getManufactureYear());
        }
        if (request.getPrice() != null) {
            vehicle.setPrice(request.getPrice());
        }
        if (request.getModelName() != null) {
            vehicle.setModelName(request.getModelName());
        }

        if (request.getOwnerId() != null) {
            VehicleOwner owner = vehicleOwnerRepo.findById(request.getOwnerId())
                    .orElseThrow(() -> new NotFoundException("Vehicle owner not found with id: " + request.getOwnerId()));
            vehicle.setCurrentOwner(owner);
        }

        if (request.getPlateNumber() != null) {
            PlateNumber newPlateNumber = plateNumberRepo.findByPlateNumber(request.getPlateNumber())
                    .orElseThrow(() -> new NotFoundException("Plate number not found: " + request.getPlateNumber()));

            if (request.getOwnerId() != null && !newPlateNumber.getVehicleOwner().getId().equals(request.getOwnerId())) {
                throw new BadRequestException("Plate number does not belong to this owner");
            }

            if (newPlateNumber.getStatus() != EPlateStatus.AVAILABLE) {
                throw new BadRequestException("Plate number is already in use");
            }

            PlateNumber oldPlateNumber = vehicle.getPlateNumber();
            if (!oldPlateNumber.getPlateNumber().equals(newPlateNumber.getPlateNumber())) {
                oldPlateNumber.setStatus(EPlateStatus.AVAILABLE);
                plateNumberRepo.save(oldPlateNumber);

                newPlateNumber.setStatus(EPlateStatus.IN_USE);
                plateNumberRepo.save(newPlateNumber);
                vehicle.setPlateNumber(newPlateNumber);
            }
        }

        Vehicle updatedVehicle = vehicleRepo.save(vehicle);
        auditLogService.logAction("Vehicle", id.toString(), "UPDATE", username,
                "Updated vehicle: " + updatedVehicle.getChassisNumber());
        // mailService.sendVehicleUpdatedEmail(username, updatedVehicle);
        LOGGER.info("Vehicle updated: {}", id);

        return ApiResponse.success("Vehicle updated successfully", HttpStatus.OK, mapToResponse(updatedVehicle));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(Long id, String username) {
        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + id));

        PlateNumber plateNumber = vehicle.getPlateNumber();
        plateNumber.setStatus(EPlateStatus.AVAILABLE);
        plateNumberRepo.save(plateNumber);

        ownershipHistoryRepo.deleteAll(ownershipHistoryRepo.findByVehicle(vehicle, Pageable.unpaged()).getContent());

        vehicleRepo.delete(vehicle);
        auditLogService.logAction("Vehicle", id.toString(), "DELETE", username,
                "Deleted vehicle: " + vehicle.getChassisNumber());
        // mailService.sendVehicleDeletedEmail(username, vehicle);
        LOGGER.info("Vehicle deleted: {}", id);

        return ApiResponse.success("Vehicle deleted successfully", HttpStatus.OK, null);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> transferVehicle(VehicleTransferDTO request, String username) {
        Vehicle vehicle;
        if (request.getVehicleIdentifier().matches("^[A-Z0-9]{6,17}$")) {
            vehicle = vehicleRepo.findByChassisNumber(request.getVehicleIdentifier())
                    .orElseThrow(() -> new NotFoundException("Vehicle not found with chassis number: " + request.getVehicleIdentifier()));
        } else {
            vehicle = vehicleRepo.findByPlateNumberPlateNumber(request.getVehicleIdentifier())
                    .orElseThrow(() -> new NotFoundException("Vehicle not found with plate number: " + request.getVehicleIdentifier()));
        }

        VehicleOwner newOwner = vehicleOwnerRepo.findById(request.getNewOwnerId())
                .orElseThrow(() -> new NotFoundException("New owner not found with id: " + request.getNewOwnerId()));

        PlateNumber oldPlateNumber = vehicle.getPlateNumber();
        String oldPlateNumberValue = oldPlateNumber.getPlateNumber();

        PlateNumber newPlateNumber = plateNumberRepo.findByPlateNumber(request.getNewPlateNumber())
                .orElseThrow(() -> new NotFoundException("New plate number not found: " + request.getNewPlateNumber()));

        if (!newPlateNumber.getVehicleOwner().getId().equals(newOwner.getId())) {
            throw new BadRequestException("New plate number does not belong to the new owner");
        }

        if (newPlateNumber.getStatus() != EPlateStatus.AVAILABLE) {
            throw new BadRequestException("New plate number is already in use");
        }

        VehicleOwnershipHistory latestHistory = ownershipHistoryRepo.findByVehicleOrderByStartDateDesc(vehicle)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No ownership history found for this vehicle"));
        latestHistory.setEndDate(LocalDateTime.now());
        ownershipHistoryRepo.save(latestHistory);

        VehicleOwnershipHistory newOwnershipHistory = new VehicleOwnershipHistory();
        newOwnershipHistory.setVehicle(vehicle);
        newOwnershipHistory.setOwner(newOwner);
        newOwnershipHistory.setStartDate(LocalDateTime.now());
        newOwnershipHistory.setPurchasePrice(request.getPurchasePrice());
        newOwnershipHistory.setPreviousPlateNumber(oldPlateNumberValue);
        newOwnershipHistory.setComments(request.getComments());
        ownershipHistoryRepo.save(newOwnershipHistory);

        oldPlateNumber.setStatus(EPlateStatus.AVAILABLE);
        plateNumberRepo.save(oldPlateNumber);

        newPlateNumber.setStatus(EPlateStatus.IN_USE);
        plateNumberRepo.save(newPlateNumber);

        vehicle.setCurrentOwner(newOwner);
        vehicle.setPlateNumber(newPlateNumber);
        Vehicle updatedVehicle = vehicleRepo.save(vehicle);

        auditLogService.logAction("Vehicle", vehicle.getId().toString(), "TRANSFER", username,
                "Transferred vehicle: " + vehicle.getChassisNumber() + " to owner: " + newOwner.getOwnerNames());
        // mailService.sendVehicleTransferredEmail(username, updatedVehicle);
        LOGGER.info("Vehicle transferred: {}", vehicle.getId());

        return ApiResponse.success("Vehicle transferred successfully", HttpStatus.OK, mapToResponse(updatedVehicle));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + id));
        return ApiResponse.success("Vehicle retrieved successfully", HttpStatus.OK, mapToResponse(vehicle));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleByChassisNumber(String chassisNumber) {
        Vehicle vehicle = vehicleRepo.findByChassisNumber(chassisNumber)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with chassis number: " + chassisNumber));
        return ApiResponse.success("Vehicle retrieved successfully", HttpStatus.OK, mapToResponse(vehicle));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleByPlateNumber(String plateNumber) {
        Vehicle vehicle = vehicleRepo.findByPlateNumberPlateNumber(plateNumber)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with plate number: " + plateNumber));
        return ApiResponse.success("Vehicle retrieved successfully", HttpStatus.OK, mapToResponse(vehicle));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleResponseDTO>>> getVehiclesByOwner(Long ownerId, Pageable pageable) {
        VehicleOwner owner = vehicleOwnerRepo.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Vehicle owner not found with id: " + ownerId));

        Page<Vehicle> vehicles = vehicleRepo.findByCurrentOwner(owner, pageable);
        Page<VehicleResponseDTO> response = vehicles.map(this::mapToResponse);

        return ApiResponse.success("Vehicles retrieved successfully", HttpStatus.OK, response);
    }

    private VehicleResponseDTO mapToResponse(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getChassisNumber(),
                vehicle.getManufactureCompany(),
                vehicle.getManufactureYear(),
                vehicle.getPrice(),
                vehicle.getModelName(),
                vehicle.getPlateNumber().getPlateNumber(),
                vehicle.getCurrentOwner().getId(),
                vehicle.getCurrentOwner().getOwnerNames(),
                vehicle.getCurrentOwner().getNationalId()
        );
    }
}