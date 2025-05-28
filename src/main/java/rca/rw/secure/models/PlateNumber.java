package rca.rw.secure.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rca.rw.secure.enums.vehicle.EPlateStatus;
import rca.rw.secure.validator.ValidPlate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plate_numbers")
@AttributeOverride(name = "createdAt", column = @Column(name = "issued_date", nullable = false))
public class PlateNumber extends Base<Long> {

    @Column(nullable = false, unique = true)
    @ValidPlate
    private String plateNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_owner_id")
    private VehicleOwner vehicleOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EPlateStatus status = EPlateStatus.AVAILABLE;

    @OneToOne(mappedBy = "plateNumber", fetch = FetchType.LAZY)
    private Vehicle vehicle;
}