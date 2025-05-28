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
import rca.rw.secure.dtos.vehicle.CreateVehicleDTO;
import rca.rw.secure.dtos.vehicle.UpdateVehicleDTO;
import rca.rw.secure.dtos.vehicle.VehicleResponseDTO;
import rca.rw.secure.dtos.vehicle.VehicleTransferDTO;
import rca.rw.secure.services.VehicleService;
import rca.rw.secure.utils.Constants;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> createVehicle(@Valid @RequestBody CreateVehicleDTO request, Authentication authentication) {
        return vehicleService.registerVehicle(request, authentication.getName());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> updateVehicle(@PathVariable Long id, @Valid @RequestBody UpdateVehicleDTO request, Authentication authentication) {
        return vehicleService.updateVehicle(id, request, authentication.getName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id, Authentication authentication) {
        return vehicleService.deleteVehicle(id, authentication.getName());
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> transferVehicle(@Valid @RequestBody VehicleTransferDTO request, Authentication authentication) {
        return vehicleService.transferVehicle(request, authentication.getName());
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping("/chassis/{chassisNumber}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleByChassisNumber(@PathVariable String chassisNumber) {
        return vehicleService.getVehicleByChassisNumber(chassisNumber);
    }

    @GetMapping("/plate/{plateNumber}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleByPlateNumber(@PathVariable String plateNumber) {
        return vehicleService.getVehicleByPlateNumber(plateNumber);
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleResponseDTO>>> getVehiclesByOwner(
            @PathVariable Long ownerId,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return vehicleService.getVehiclesByOwner(ownerId, pageable);
    }
}