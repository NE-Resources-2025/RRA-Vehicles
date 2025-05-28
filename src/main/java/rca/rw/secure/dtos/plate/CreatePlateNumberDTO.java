package rca.rw.secure.dtos.plate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlateNumberDTO {
    @NotBlank(message = "Plate number is required")
    @Schema(description = "The plateNumber of the plate", required = true)
    @Pattern(regexp = "^[A-Z0-9]{2,3}\\s?[0-9]{3}[A-Z]$", message = "Plate number must be in the format RAA123A")
    private String plateNumber;

    @NotNull(message = "Owner ID is required")
    @Schema(description = "The owner's id", required = true)
    private Long ownerId;
}