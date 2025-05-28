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
import rca.rw.secure.dtos.plate.CreatePlateNumberDTO;
import rca.rw.secure.dtos.plate.PlateNumberResponseDTO;
import rca.rw.secure.dtos.plate.UpdatePlateNumberDTO;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.services.PlateNumberService;
import rca.rw.secure.utils.Constants;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RequestMapping("/api/v1/plate-numbers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class PlateNumberController {

    private final PlateNumberService plateNumberService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> createPlateNumber(@Valid @RequestBody CreatePlateNumberDTO request, Authentication authentication) {
        return plateNumberService.registerPlateNumber(request, authentication.getName());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> updatePlateNumber(@PathVariable Long id, @Valid @RequestBody UpdatePlateNumberDTO request, Authentication authentication) {
        return plateNumberService.updatePlateNumber(id, request, authentication.getName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlateNumber(@PathVariable Long id, Authentication authentication) {
        return plateNumberService.deletePlateNumber(id, authentication.getName());
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<PlateNumberResponseDTO>>> getAllPlateNumbers(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return plateNumberService.getAllPlateNumbers(pageable);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> getPlateNumberById(@PathVariable Long id) {
        return plateNumberService.getPlateNumberById(id);
    }

    @GetMapping("/number/{plateNumber}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<PlateNumberResponseDTO>> getPlateNumberByPlateNumber(@PathVariable String value) {
        return plateNumberService.getPlateNumberByPlateNumber(value);
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<Page<PlateNumberResponseDTO>>> getPlateNumbersByOwnerId(
            @PathVariable Long ownerId,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return plateNumberService.getPlateNumbersByOwner(ownerId, pageable);
    }

    @GetMapping("/owner/{ownerId}/available")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<List<PlateNumberResponseDTO>>> getAvailablePlateNumbersByOwner(@PathVariable Long ownerId) {
        return plateNumberService.getAvailablePlateNumbersByOwner(ownerId);
    }
}