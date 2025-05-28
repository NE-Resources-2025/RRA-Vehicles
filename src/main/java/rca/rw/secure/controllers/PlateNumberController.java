package com.rra.vehicletracking.controller;

import com.rra.vehicletracking.dto.PlateNumberDTOs.PlateNumberRequest;
import com.rra.vehicletracking.dto.PlateNumberDTOs.PlateNumberResponse;
import com.rra.vehicletracking.service.PlateNumberService;
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
import java.util.List;

@RestController
@RequestMapping("/api/plate-numbers")
@CrossOrigin(origins = "*")
@Tag(name = "Plate Numbers", description = "Endpoints for managing plate numbers")
public class PlateNumberController {

    @Autowired
    private PlateNumberService plateNumberService;

    @PostMapping
    @Operation(
            summary = "Register a new plate number",
            description = "Register a new plate number (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Plate number registered", content = @Content(schema = @Schema(implementation = PlateNumberResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> registerPlateNumber(@Valid @RequestBody PlateNumberRequest request) {
        try {
            PlateNumberResponse response = plateNumberService.registerPlateNumber(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing plate number",
            description = "Update the details of an existing plate number (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plate number updated", content = @Content(schema = @Schema(implementation = PlateNumberResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Plate number not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updatePlateNumber(@PathVariable Long id, @Valid @RequestBody PlateNumberRequest request) {
        try {
            PlateNumberResponse response = plateNumberService.updatePlateNumber(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a plate number",
            description = "Delete a plate number by its ID (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plate number deleted successfully", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Plate number not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deletePlateNumber(@PathVariable Long id) {
        try {
            plateNumberService.deletePlateNumber(id);
            return ResponseEntity.ok("Plate number deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get plate numbers by owner",
            description = "Retrieve paginated plate numbers for a specific owner",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "Owner not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getPlateNumbersByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<PlateNumberResponse> plateNumbers = plateNumberService.getPlateNumbersByOwner(ownerId, pageable);
            return ResponseEntity.ok(plateNumbers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}/available")
    @Operation(
            summary = "Get available plate numbers by owner",
            description = "Retrieve available plate numbers for a specific owner",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "404", description = "Owner not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getAvailablePlateNumbersByOwner(@PathVariable Long ownerId) {
        try {
            List<PlateNumberResponse> availablePlateNumbers = plateNumberService.getAvailablePlateNumbersByOwner(ownerId);
            return ResponseEntity.ok(availablePlateNumbers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get plate number by ID",
            description = "Retrieve a plate number by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = PlateNumberResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Plate number not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getPlateNumberById(@PathVariable Long id) {
        try {
            PlateNumberResponse response = plateNumberService.getPlateNumberById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/value/{plateNumber}")
    @Operation(
            summary = "Get plate number by value",
            description = "Retrieve a plate number by its value",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = PlateNumberResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Plate number not found", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getPlateNumberByValue(@PathVariable String plateNumber) {
        try {
            PlateNumberResponse response = plateNumberService.getPlateNumberByPlateNumber(plateNumber);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}