package rca.rw.secure.dtos.vehicle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateVehicleDTO {
    @NotBlank(message = "Chassis number is required")
    @Schema(description = "Unique chassis number of the vehicle", example = "AN123456789", required = true)
    private String chassisNumber;

    @NotBlank(message = "Manufacture company is required")
    @Schema(description = "Company that manufactured the vehicle", example = "Toyota", required = true)
    private String manufactureCompany;

    @NotNull(message = "Manufacture year is required")
    @PastOrPresent(message = "Manufacture year cannot be in the future")
    @Schema(description = "Year the vehicle was manufactured", example = "2005", required = true)
    private Year manufactureYear;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Schema(description = "Purchase price of the vehicle", example = "13000000", required = true)
    private BigDecimal price;

    @NotBlank(message = "Model name is required")
    @Schema(description = "Model name of the vehicle", example = "RAV4", required = true)
    private String modelName;

    @NotNull(message = "Owner ID is required")
    @Schema(description = "ID of the vehicle owner", example = "1", required = true)
    private Long ownerId;

    @NotBlank(message = "Plate number is required")
    @Schema(description = "Plate number assigned to the vehicle", example = "RAA123A", required = true)
    private String plateNumber;
}