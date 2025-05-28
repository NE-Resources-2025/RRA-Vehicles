package rca.rw.secure.dtos.vehicleOwnerHistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleOwnershipHistoryDTO {
    @NotNull(message = "Vehicle ID is required")
    @Schema(description = "ID of the vehicle", example = "1", required = true)
    private Long vehicleId;

    @NotNull(message = "Owner ID is required")
    @Schema(description = "ID of the vehicle owner", example = "1", required = true)
    private Long ownerId;

    @NotNull(message = "Start date is required")
    @Schema(description = "Date when ownership started", example = "2025-05-28T12:00:00", required = true)
    private LocalDateTime startDate;

    @Positive(message = "Purchase price must be positive")
    @Schema(description = "Price at which the vehicle was purchased", example = "13000000", required = true)
    private BigDecimal purchasePrice;

    @Schema(description = "Previous plate number, if applicable", example = "RAA123A", required = false)
    private String previousPlateNumber;

    @Schema(description = "Additional comments about the ownership", example = "Vehicle transferred from previous owner", required = false)
    private String comments;
}