package rca.rw.secure.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import rca.rw.secure.enums.user.EUserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rca.rw.secure.validator.ValidRwandaId;
import rca.rw.secure.validator.ValidRwandanPhoneNumber;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"phone"}),
        @UniqueConstraint(columnNames = {"national_id"})
})
@NoArgsConstructor
@AllArgsConstructor
public class User extends Base <Long>{

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Transient
    private String fullName;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Column(name = "phone")
    @ValidRwandanPhoneNumber
    private String phone;

    @Column(name = "national_id")
    @ValidRwandaId
    private String nationalId;

    @JsonIgnore
    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EUserStatus status = EUserStatus.ACTIVE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String username, String email, String password, EUserStatus status, String phone, String nationalId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.phone = phone;
        this.nationalId = nationalId;
    }
}
