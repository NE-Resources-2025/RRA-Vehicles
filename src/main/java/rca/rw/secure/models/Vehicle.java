package rca.rw.secure.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import rca.rw.secure.validator.ValidChassis;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicles", uniqueConstraints = {@UniqueConstraint(columnNames = {"chassisNumber"})})
public class Vehicle extends Base <Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @ValidChassis
    private String chassisNumber;

    @Column(nullable = false)
    private String manufactureCompany;

    @Column(nullable = false)
    private Year manufactureYear;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String modelName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plate_number_id")
    private PlateNumber plateNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_owner_id")
    private VehicleOwner currentOwner;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehicleOwnershipHistory> ownershipHistory = new ArrayList<>();

//    public Vehicle(String chassisNumber, String manufactureCompany, Integer manufactureYear,
//                   BigDecimal price, String modelName, PlateNumber plateNumber, VehicleOwner currentOwner,
//                   List<VehicleOwnershipHistory> ownershipHistory) {
//        this.chassisNumber = chassisNumber;
//        this.manufactureCompany = manufactureCompany;
//        this.manufactureYear = manufactureYear;
//        this.price = price;
//        this.modelName = modelName;
//        this.plateNumber = plateNumber;
//        this.currentOwner = currentOwner;
//        this.ownershipHistory = ownershipHistory;
//    }

}