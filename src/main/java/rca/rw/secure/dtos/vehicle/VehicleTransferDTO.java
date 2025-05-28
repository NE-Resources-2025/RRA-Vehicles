package rca.rw.secure.dtos.vehicle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTransferDTO {
    @NotBlank(message = "Vehicle identifier is required")
    @Schema(description = "Chassis number or plate number of the vehicle", example = "AN123456789 or RAA123A", required = true)
    private String vehicleIdentifier;

    @NotNull(message = "New owner ID is required")
    @Schema(description = "ID of the new vehicle owner", example = "2", required = true)
    private Long newOwnerId;

    @NotBlank(message = "New plate number is required")
    @Schema(description = "New plate number for the vehicle", example = "RAB456B", required = true)
    private String newPlateNumber;

    @NotNull(message = "Purchase price is required")
    @Positive(message = "Purchase price must be positive")
    @Schema(description = "Price at which the vehicle was purchased", example = "15000000", required = true)
    private BigDecimal purchasePrice;

    @Schema(description = "Comments about the transfer", example = "Vehicle transferred to new owner", required = false)
    private String comments;
}