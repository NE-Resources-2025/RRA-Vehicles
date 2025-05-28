package rca.rw.secure.dtos.role;

import rca.rw.secure.enums.user.EUserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateRoleDTO {
    @Schema(example = "ROLE_ADMIN", description = "Role name")
//    @NotBlank(message = "Role name is required")
    private EUserRole name;
}
