package rca.rw.secure.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rca.rw.secure.validator.ValidRwandaId;
import rca.rw.secure.validator.ValidRwandanPhoneNumber;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "vehicle_owners", uniqueConstraints = {@UniqueConstraint(columnNames = {"phoneNumber"})})
public class VehicleOwner extends Base<Long> {

    @Column(nullable = false)
    private String ownerNames;

    @Column(nullable = false, unique = true)
    @ValidRwandaId
    private String nationalId;

    @Column(nullable = false)
    @ValidRwandanPhoneNumber
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "national_id", columnDefinition = "VARCHAR(16)", foreignKey = @ForeignKey(name = "FK_vehicle_owner_user"))
    private User user;

    @OneToMany(mappedBy = "vehicleOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlateNumber> plateNumbers = new ArrayList<>();

    public VehicleOwner(Long id, String ownerNames, String nationalId, String phoneNumber,
                        String address, List<PlateNumber> plateNumbers) {
        this.ownerNames = ownerNames;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.plateNumbers = plateNumbers;
    }
}