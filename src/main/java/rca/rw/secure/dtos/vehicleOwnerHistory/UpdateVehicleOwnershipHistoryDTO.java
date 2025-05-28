package rca.rw.secure.dtos.vehicleOwnerHistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleOwnershipHistoryDTO {
    @Schema(description = "ID of the vehicle", example = "456", required = false)
    private Long vehicleId;

    @Schema(description = "ID of the vehicle owner", example = "1", required = false)
    private Long ownerId;

    @Schema(description = "Date when ownership started", example = "2025-05-28T12:00:00", required = false)
    private LocalDateTime startDate;

    @Schema(description = "Date when ownership ended", example = "2025-12-31T12:00:00", required = false)
    private LocalDateTime endDate;

    @Positive
    @Schema(description = "Price at which the vehicle was purchased", example = "13000000", required = false)
    private BigDecimal purchasePrice;

    @Schema(description = "Previous plate number, if applicable", example = "RAA123A", required = false)
    private String previousPlateNumber;

    @Schema(description = "Additional comments about the ownership", example = "Updated ownership details", required = false)
    private String comments;
}