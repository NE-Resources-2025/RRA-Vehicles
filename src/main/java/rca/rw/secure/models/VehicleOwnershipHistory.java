package com.rra.vehicletracking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_ownership_history")
public class VehicleOwnershipHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public VehicleOwnershipHistory() {
    }

    public VehicleOwnershipHistory(Long id, Vehicle vehicle, VehicleOwner owner, LocalDateTime startDate,
                                   LocalDateTime endDate, BigDecimal purchasePrice, String previousPlateNumber,
                                   String comments) {
        this.id = id;
        this.vehicle = vehicle;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
        this.purchasePrice = purchasePrice;
        this.previousPlateNumber = previousPlateNumber;
        this.comments = comments;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public VehicleOwner getOwner() {
        return owner;
    }

    public void setOwner(VehicleOwner owner) {
        this.owner = owner;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getPreviousPlateNumber() {
        return previousPlateNumber;
    }

    public void setPreviousPlateNumber(String previousPlateNumber) {
        this.previousPlateNumber = previousPlateNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}