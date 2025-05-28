package rca.rw.secure.dtos.book;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for creating a new book")
public class CreateBookDTO {

    @NotBlank(message = "Name is mandatory")
    @Schema(description = "The name of the book", required = true)
    private String name;

    @Schema(description = "A brief description of the book")
    private String description;
}
