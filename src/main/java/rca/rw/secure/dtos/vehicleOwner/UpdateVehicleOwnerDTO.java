package rca.rw.secure.dtos.vehicleOwner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleOwnerDTO {
    @Schema(description = "Full names of the vehicle owner", example = "John Doe", required = false)
    private String ownerNames;

    @Pattern(regexp = "^[0-9]{16}$", message = "National ID must be 16 digits")
    @Schema(description = "Unique national ID of the owner", example = "1234567890123456", required = false)
    private String nationalId;

    @Pattern(regexp = "^\\+07[0-8]{9}$", message = "Phone number must be in format +07XXXXXXXX")
    @Schema(description = "Phone number of the owner", example = "+250788123456", required = false)
    private String phoneNumber;

    @Schema(description = "Residential address of the owner", example = "Kigali, Rwanda", required = false)
    private String address;
}