package rca.rw.secure.dtos.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserDTO {
    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "johndoe")
    private String username;

    @Schema(example = "example@gmail.com")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+250[0-9]{9}$", message = "Phone number must be in format +250XXXXXXXXX")
    @Schema(description = "Phone number of the owner", example = "+250788123456", required = true)
    private String phoneNumber;

    @Schema(example = "1200080000123042")
    private String nationalId;

}


