package rca.rw.secure.models;

import rca.rw.secure.enums.user.EUserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Base <Long>{

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private EUserRole name;
}
