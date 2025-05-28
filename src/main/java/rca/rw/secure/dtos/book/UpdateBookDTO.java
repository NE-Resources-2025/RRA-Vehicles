package rca.rw.secure.dtos.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBookDTO {

    @NotBlank(message = "Name is mandatory")
    @Schema(description = "The name of the book", required = true)
    private String name;

    @Schema(description = "A brief description of the book")
    private String description;
}