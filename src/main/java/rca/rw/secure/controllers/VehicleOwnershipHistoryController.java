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
import rca.rw.secure.dtos.vehicleOwnerHistory.CreateVehicleOwnershipHistoryDTO;
import rca.rw.secure.dtos.vehicleOwnerHistory.UpdateVehicleOwnershipHistoryDTO;
import rca.rw.secure.dtos.vehicleOwnerHistory.VehicleOwnershipHistoryResponseDTO;
import rca.rw.secure.services.VehicleOwnershipHistoryService;
import rca.rw.secure.utils.Constants;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RequestMapping("/api/v1/ownership-history")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class VehicleOwnershipHistoryController {

    private final VehicleOwnershipHistoryService ownershipHistoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleOwnershipHistoryResponseDTO>> createOwnershipHistory(@Valid @RequestBody CreateVehicleOwnershipHistoryDTO request, Authentication authentication) {
        return ownershipHistoryService.createOwnershipHistory(request, authentication.getName());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<VehicleOwnershipHistoryResponseDTO>> updateOwnershipHistory(@PathVariable Long id, @Valid @RequestBody UpdateVehicleOwnershipHistoryDTO request, Authentication authentication) {
        return ownershipHistoryService.updateOwnershipHistory(id, request, authentication.getName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOwnershipHistory(@PathVariable Long id, Authentication authentication) {
        return ownershipHistoryService.deleteOwnershipHistory(id, authentication.getName());
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByVehicleId(
            @PathVariable Long vehicleId,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return ownershipHistoryService.getHistoryByVehicleId(vehicleId, pageable);
    }

    @GetMapping("/chassis/{chassisNumber}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByChassisNumber(
            @PathVariable String chassisNumber,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return ownershipHistoryService.getHistoryByChassisNumber(chassisNumber, pageable);
    }

    @GetMapping("/plate/{plateNumber}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByPlateNumber(
            @PathVariable String plateNumber,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return ownershipHistoryService.getHistoryByPlateNumber(plateNumber, pageable);
    }
}