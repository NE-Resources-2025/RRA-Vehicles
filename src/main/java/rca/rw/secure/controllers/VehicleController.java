package com.rra.vehicletracking.controller;

import com.rra.vehicletracking.dto.VehicleDTOs.VehicleRegistrationRequest;
import com.rra.vehicletracking.dto.VehicleDTOs.VehicleResponse;
import com.rra.vehicletracking.dto.VehicleDTOs.VehicleTransferRequest;
import com.rra.vehicletracking.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
@Tag(name = "Vehicles", description = "Endpoints for managing vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    @Operation(
            summary = "Register a new vehicle",
            description = "Register a new vehicle (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vehicle registered", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> registerVehicle(@Valid @RequestBody VehicleRegistrationRequest request) {
        try {
            VehicleResponse response = vehicleService.registerVehicle(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing vehicle",
            description = "Update the details of an existing vehicle (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle updated", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleRegistrationRequest request) {
        try {
            VehicleResponse response = vehicleService.updateVehicle(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a vehicle",
            description = "Delete a vehicle by its ID (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle deleted successfully", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.ok("Vehicle deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    @Operation(
            summary = "Transfer a vehicle",
            description = "Transfer ownership of a vehicle (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle transferred", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> transferVehicle(@Valid @RequestBody VehicleTransferRequest request) {
        try {
            VehicleResponse response = vehicleService.transferVehicle(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/chassis/{chassisNumber}")
    @Operation(
            summary = "Get vehicle by chassis number",
            description = "Retrieve a vehicle by its chassis number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getVehicleByChassisNumber(@PathVariable String chassisNumber) {
        try {
            VehicleResponse response = vehicleService.getVehicleByChassisNumber(chassisNumber);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/plate/{plateNumber}")
    @Operation(
            summary = "Get vehicle by plate number",
            description = "Retrieve a vehicle by its plate number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getVehicleByPlateNumber(@PathVariable String plateNumber) {
        try {
            VehicleResponse response = vehicleService.getVehicleByPlateNumber(plateNumber);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get vehicles by owner",
            description = "Retrieve paginated vehicles for a specific owner",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "Owner not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getVehiclesByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<VehicleResponse> vehicles = vehicleService.getVehiclesByOwner(ownerId, pageable);
            return ResponseEntity.ok(vehicles);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}