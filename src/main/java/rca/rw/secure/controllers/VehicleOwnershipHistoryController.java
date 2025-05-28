package com.rra.vehicletracking.controller;

import com.rra.vehicletracking.dto.VehicleOwnershipHistoryDTOs.OwnershipHistoryResponse;
import com.rra.vehicletracking.service.VehicleOwnershipHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ownership-history")
@CrossOrigin(origins = "*")
@Tag(name = "Ownership History", description = "Endpoints for managing vehicle ownership history")
public class VehicleOwnershipHistoryController {

    @Autowired
    private VehicleOwnershipHistoryService ownershipHistoryService;

    @PostMapping
    @Operation(
            summary = "Create a new ownership history record",
            description = "Create a new ownership history record (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ownership history created", content = @Content(schema = @Schema(implementation = OwnershipHistoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createOwnershipHistory(@Valid @RequestBody OwnershipHistoryResponse request) {
        try {
            OwnershipHistoryResponse response = ownershipHistoryService.createOwnershipHistory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing ownership history record",
            description = "Update the details of an existing ownership history record (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ownership history updated", content = @Content(schema = @Schema(implementation = OwnershipHistoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Ownership history not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateOwnershipHistory(@PathVariable Long id, @Valid @RequestBody OwnershipHistoryResponse request) {
        try {
            OwnershipHistoryResponse response = ownershipHistoryService.updateOwnershipHistory(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an ownership history record",
            description = "Delete an ownership history record by its ID (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ownership history deleted successfully", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Ownership history not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteOwnershipHistory(@PathVariable Long id) {
        try {
            ownershipHistoryService.deleteOwnershipHistory(id);
            return ResponseEntity.ok("Ownership history deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(
            summary = "Get ownership history by vehicle ID",
            description = "Retrieve paginated ownership history for a specific vehicle ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getHistoryByVehicleId(
            @PathVariable Long vehicleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
            Page<OwnershipHistoryResponse> history = ownershipHistoryService.getHistoryByVehicleId(vehicleId, pageable);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/chassis/{chassisNumber}")
    @Operation(
            summary = "Get ownership history by chassis number",
            description = "Retrieve paginated ownership history for a specific chassis number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getHistoryByChassisNumber(
            @PathVariable String chassisNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
            Page<OwnershipHistoryResponse> history = ownershipHistoryService.getHistoryByChassisNumber(chassisNumber, pageable);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/plate/{plateNumber}")
    @Operation(
            summary = "Get ownership history by plate number",
            description = "Retrieve paginated ownership history for a specific plate number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getHistoryByPlateNumber(
            @PathVariable String plateNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
            Page<OwnershipHistoryResponse> history = ownershipHistoryService.getHistoryByPlateNumber(plateNumber, pageable);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}