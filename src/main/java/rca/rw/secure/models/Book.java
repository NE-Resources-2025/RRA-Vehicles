package rca.rw.secure.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resources")
@Data
public class Resource extends Base <UUID>{
    @Column(nullable = false)
    private String name;

    private String description;
}