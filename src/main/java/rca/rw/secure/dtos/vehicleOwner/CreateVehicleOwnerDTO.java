package rca.rw.secure.dtos.vehicleOwner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleOwnerDTO {
    @NotBlank(message = "Owner names are required")
    @Schema(description = "Full names of the vehicle owner", example = "John Doe", required = true)
    private String ownerNames;

    @NotBlank(message = "National ID is required")
    @Pattern(regexp = "^[0-9]{16}$", message = "National ID must be 16 digits")
    @Schema(description = "Unique national ID of the owner", example = "1234567890123456", required = true)
    private String nationalId;

    @NotBlank(message = "Phone number is required")
    @Schema(description = "Phone number of the owner", example = "+250788123456", required = true)
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Schema(description = "Residential address of the owner", example = "Kigali, Rwanda", required = true)
    private String address;
}
