package rca.rw.secure.dtos.vehicle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleDTO {
    @Schema(description = "Unique chassis number of the vehicle", example = "AN123456789", required = false)
    private String chassisNumber;

    @Schema(description = "Company that manufactured the vehicle", example = "Toyota", required = false)
    private String manufactureCompany;

    @PastOrPresent(message = "Manufacture year cannot be in the future")
    @Schema(description = "Year the vehicle was manufactured", example = "2005", required = false)
    private Year manufactureYear;

    @Positive(message = "Price must be positive")
    @Schema(description = "Purchase price of the vehicle", example = "13000000", required = false)
    private BigDecimal price;

    @Schema(description = "Model name of the vehicle", example = "RAV4", required = false)
    private String modelName;

    @Schema(description = "ID of the vehicle owner", example = "1", required = false)
    private Long ownerId;

    @Schema(description = "Plate number assigned to the vehicle", example = "RAA123A", required = false)
    private String plateNumber;
}