package rca.rw.secure.dtos.resource;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResourceResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}