package rca.rw.secure.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.vehicleOwner.CreateVehicleOwnerDTO;
import rca.rw.secure.dtos.vehicleOwner.UpdateVehicleOwnerDTO;
import rca.rw.secure.dtos.vehicleOwner.VehicleOwnerResponseDTO;
import rca.rw.secure.services.VehicleOwnerService;
import rca.rw.secure.utils.Constants;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RequestMapping("/api/v1/vehicle-owners")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class VehicleOwnerController {

    private final VehicleOwnerService vehicleOwnerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> createVehicleOwner(@Valid @RequestBody CreateVehicleOwnerDTO request, Authentication authentication) {
        return vehicleOwnerService.registerVehicleOwner(request, authentication.getName());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> updateVehicleOwner(@PathVariable Long id, @Valid @RequestBody UpdateVehicleOwnerDTO request, Authentication authentication) {
        return vehicleOwnerService.updateVehicleOwner(id, request, authentication.getName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicleOwner(@PathVariable Long id, Authentication authentication) {
        return vehicleOwnerService.deleteVehicleOwner(id, authentication.getName());
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> getAllVehicleOwners(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return vehicleOwnerService.getAllVehicleOwners(pageable);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> getVehicleOwnerById(@PathVariable Long id) {
        return vehicleOwnerService.getVehicleOwnerById(id);
    }

    @GetMapping("/national-id/{nationalId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> getVehicleOwnerByNationalId(@PathVariable String nationalId) {
        return vehicleOwnerService.getVehicleOwnerByNationalId(nationalId);
    }

    @GetMapping("/search/national-id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByNationalId(
            @RequestParam String nationalId,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return vehicleOwnerService.searchVehicleOwnersByNationalId(nationalId, pageable);
    }

    @GetMapping("/search/phone")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByPhoneNumber(
            @RequestParam String phoneNumber,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return vehicleOwnerService.searchVehicleOwnersByPhoneNumber(phoneNumber, pageable);
    }

    @GetMapping("/search/email")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByEmail(
            @RequestParam String email,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return vehicleOwnerService.searchVehicleOwnersByEmail(email, pageable);
    }
}