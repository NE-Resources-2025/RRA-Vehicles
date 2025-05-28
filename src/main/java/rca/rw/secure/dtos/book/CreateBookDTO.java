package rca.rw.secure.dtos.resource;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateResourceDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;
}