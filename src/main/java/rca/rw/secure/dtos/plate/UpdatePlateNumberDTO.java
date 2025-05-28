package rca.rw.secure.dtos.plate;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rca.rw.secure.enums.vehicle.EPlateStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlateNumberDTO {

    @Schema(description = "The plate number in the format RAA 123A",
            example = "RAA 123A",
            required = true)
    @Pattern(regexp = "^[A-Z0-9]{2,3}\\s?[0-9]{3}[A-Z]$", message = "Plate number must be in the format RAA 123A")
    private String plateNumber;

    @Schema(description = "The ID of the owner of the plate",
            example = "12345",
            required = true)
    private Long ownerId;

    @Schema(description = "The status of the plate",
            example = "ACTIVE",
            required = true)
    private EPlateStatus status;
}
