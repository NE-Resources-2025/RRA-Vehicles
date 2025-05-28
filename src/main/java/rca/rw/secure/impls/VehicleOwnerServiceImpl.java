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
import rca.rw.secure.dtos.vehicleOwner.CreateVehicleOwnerDTO;
import rca.rw.secure.dtos.vehicleOwner.UpdateVehicleOwnerDTO;
import rca.rw.secure.dtos.vehicleOwner.VehicleOwnerResponseDTO;
import rca.rw.secure.exceptions.BadRequestException;
import rca.rw.secure.exceptions.NotFoundException;
import rca.rw.secure.models.User;
import rca.rw.secure.models.VehicleOwner;
import rca.rw.secure.repos.IUserRepo;
import rca.rw.secure.repos.IVehicleOwnerRepo;
import rca.rw.secure.services.AuditLogService;
import rca.rw.secure.services.MailService;
import rca.rw.secure.services.VehicleOwnerService;

@Service
@RequiredArgsConstructor
public class VehicleOwnerServiceImpl implements VehicleOwnerService {
    private final IVehicleOwnerRepo vehicleOwnerRepo;
    private final AuditLogService auditLogService;
    private final MailService mailService;
    private final IUserRepo userRepo;
    private static final Logger logger = LoggerFactory.getLogger(VehicleOwnerServiceImpl.class);

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> registerVehicleOwner(CreateVehicleOwnerDTO request, String username) {
        if (vehicleOwnerRepo.existsByNationalId(request.getNationalId())) {
            throw new BadRequestException("National ID already exists: " + request.getNationalId());
        }
        if (vehicleOwnerRepo.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException("Phone number already exists: " + request.getPhoneNumber());
        }

        User user = userRepo.findByNationalId(request.getNationalId())
                .orElseThrow(() -> new NotFoundException("User not found with national ID: " + request.getNationalId()));

        VehicleOwner owner = new VehicleOwner();
        owner.setOwnerNames(request.getOwnerNames());
        owner.setNationalId(request.getNationalId());
        owner.setPhoneNumber(request.getPhoneNumber());
        owner.setAddress(request.getAddress());

        VehicleOwner savedOwner = vehicleOwnerRepo.save(owner);

//        auditLogService.logAction("VehicleOwner", savedOwner.getId().toString(), "CREATE", username,
//                "Created owner: " + savedOwner.getOwnerNames());
//        mailService.sendVehicleOwnerCreatedEmail(username, savedOwner);
        logger.info("Vehicle owner created: {}", savedOwner.getId());

        return ApiResponse.success("Vehicle owner created successfully", HttpStatus.CREATED, mapToResponse(savedOwner));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> updateVehicleOwner(Long id, UpdateVehicleOwnerDTO request, String username) {
        VehicleOwner owner = vehicleOwnerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle owner not found with ID: " + id));

        if (request.getOwnerNames() != null) {
            owner.setOwnerNames(request.getOwnerNames());
        }
        if (request.getNationalId() != null && !request.getNationalId().equals(owner.getNationalId())) {
            if (vehicleOwnerRepo.existsByNationalId(request.getNationalId())) {
                throw new BadRequestException("National ID already exists: " + request.getNationalId());
            }
            User user = userRepo.findByNationalId(request.getNationalId())
                    .orElseThrow(() -> new NotFoundException("User not found with national ID: " + request.getNationalId()));
            owner.setUser(user);
            owner.setNationalId(request.getNationalId());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(owner.getPhoneNumber())) {
            if (vehicleOwnerRepo.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new BadRequestException("Phone number already exists: " + request.getPhoneNumber());
            }
            owner.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            owner.setAddress(request.getAddress());
        }

        VehicleOwner updatedOwner = vehicleOwnerRepo.save(owner);
        auditLogService.logAction("VehicleOwner", id.toString(), "UPDATE", username,
                "Updated owner: " + updatedOwner.getOwnerNames());
//        mailService.sendVehicleOwnerUpdatedEmail(username, updatedOwner);
        logger.info("Vehicle owner updated: {}", id);

        return ApiResponse.success("Vehicle owner updated successfully", HttpStatus.OK, mapToResponse(updatedOwner));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVehicleOwner(Long id, String username) {
        VehicleOwner owner = vehicleOwnerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Owner not found with ID: " + id));

        if (!owner.getPlateNumbers().isEmpty()) {
            throw new BadRequestException("Cannot delete owner with associated plate numbers");
        }

        vehicleOwnerRepo.delete(owner);
        auditLogService.logAction("VehicleOwner", id.toString(), "DELETE", username,
                "Deleted owner: " + owner.getOwnerNames());
//        mailService.sendVehicleOwnerDeletedEmail(username, owner);
        logger.info("Vehicle owner deleted: {}", id);

        return ApiResponse.success("Vehicle owner deleted successfully", HttpStatus.OK, null);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> getAllVehicleOwners(Pageable pageable) {
        Page<VehicleOwner> owners = vehicleOwnerRepo.findAll(pageable);
        Page<VehicleOwnerResponseDTO> response = owners.map(this::mapToResponse);
        return ApiResponse.success("Vehicle owners retrieved successfully", HttpStatus.OK, response);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> getVehicleOwnerById(Long id) {
        VehicleOwner owner = vehicleOwnerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Owner not found with ID: " + id));
        return ApiResponse.success("Success", HttpStatus.OK, mapToResponse(owner));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> getVehicleOwnerByNationalId(String nationalId) {
        VehicleOwner owner = vehicleOwnerRepo.findByNationalId(nationalId)
                .orElseThrow(() -> new NotFoundException("Owner not found with national ID: " + nationalId));
        return ApiResponse.success("Success", HttpStatus.OK, mapToResponse(owner));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByNationalId(String nationalId, Pageable pageable) {
        Page<VehicleOwner> owners = vehicleOwnerRepo.findByNationalIdContaining(nationalId, pageable);
        Page<VehicleOwnerResponseDTO> response = owners.map(this::mapToResponse);
        return ApiResponse.success("Vehicle owners retrieved successfully", HttpStatus.OK, response);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByPhoneNumber(String phoneNumber, Pageable pageable) {
        Page<VehicleOwner> owners = vehicleOwnerRepo.findByPhoneNumberContaining(phoneNumber, pageable);
        Page<VehicleOwnerResponseDTO> response = owners.map(this::mapToResponse);
        return ApiResponse.success("Vehicle owners retrieved successfully", HttpStatus.OK, response);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByEmail(String email, Pageable pageable) {
        Page<VehicleOwner> owners = vehicleOwnerRepo.findByUserEmailContaining(email, pageable);
        Page<VehicleOwnerResponseDTO> response = owners.map(this::mapToResponse);
        return ApiResponse.success("Success: Vehicle owners retrieved successfully", HttpStatus.OK, response);
    }

    private VehicleOwnerResponseDTO mapToResponse(VehicleOwner owner) {
        return new VehicleOwnerResponseDTO(
                owner.getId(),
                owner.getOwnerNames(),
                owner.getNationalId(),
                owner.getPhoneNumber(),
                owner.getAddress()
        );
    }
}