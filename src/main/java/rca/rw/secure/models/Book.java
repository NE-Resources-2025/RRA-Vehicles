package rca.rw.secure.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "resources")
@Data
public class Book extends Base <UUID>{
    @Column(nullable = false)
    private String name;

    private String description;
}