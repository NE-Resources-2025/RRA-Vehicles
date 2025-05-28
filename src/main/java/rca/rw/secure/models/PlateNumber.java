package com.rra.vehicletracking.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "plate_numbers")
public class PlateNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String plateNumber;

    @Column(nullable = false)
    private LocalDate issuedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_owner_id")
    private VehicleOwner vehicleOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlateStatus status;

    @OneToOne(mappedBy = "plateNumber", fetch = FetchType.LAZY)
    private Vehicle vehicle;

    public PlateNumber() {
    }

    public PlateNumber(Long id, String plateNumber, LocalDate issuedDate, VehicleOwner vehicleOwner,
                       User user, PlateStatus status, Vehicle vehicle) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.issuedDate = issuedDate;
        this.vehicleOwner = vehicleOwner;
        this.user = user;
        this.status = status;
        this.vehicle = vehicle;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public VehicleOwner getVehicleOwner() {
        return vehicleOwner;
    }

    public void setVehicleOwner(VehicleOwner vehicleOwner) {
        this.vehicleOwner = vehicleOwner;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PlateStatus getStatus() {
        return status;
    }

    public void setStatus(PlateStatus status) {
        this.status = status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public enum PlateStatus {
        AVAILABLE,
        IN_USE
    }
}