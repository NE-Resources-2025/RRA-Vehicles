package rca.rw.secure.dtos.book;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}