package rca.rw.secure.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rca.rw.secure.validator.ValidPlate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "vehicle_ownership_history")
public class VehicleOwnershipHistory extends Base <Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private VehicleOwner owner;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    private BigDecimal purchasePrice;

    private String previousPlateNumber;

    private String comments;

//    public VehicleOwnershipHistory(Long id, Vehicle vehicle, VehicleOwner owner, LocalDateTime startDate,
//                                   LocalDateTime endDate, BigDecimal purchasePrice, String previousPlateNumber,
//                                   String comments) {
//        this.id = id;
//        this.vehicle = vehicle;
//        this.owner = owner;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.purchasePrice = purchasePrice;
//        this.previousPlateNumber = previousPlateNumber;
//        this.comments = comments;
//    }

}