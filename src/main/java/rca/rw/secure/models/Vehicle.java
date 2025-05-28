package com.rra.vehicletracking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String chassisNumber;

    @Column(nullable = false)
    private String manufactureCompany;

    @Column(nullable = false)
    private Integer manufactureYear;

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

    public Vehicle() {
    }

    public Vehicle(Long id, String chassisNumber, String manufactureCompany, Integer manufactureYear,
                   BigDecimal price, String modelName, PlateNumber plateNumber, VehicleOwner currentOwner,
                   List<VehicleOwnershipHistory> ownershipHistory) {
        this.id = id;
        this.chassisNumber = chassisNumber;
        this.manufactureCompany = manufactureCompany;
        this.manufactureYear = manufactureYear;
        this.price = price;
        this.modelName = modelName;
        this.plateNumber = plateNumber;
        this.currentOwner = currentOwner;
        this.ownershipHistory = ownershipHistory;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getManufactureCompany() {
        return manufactureCompany;
    }

    public void setManufactureCompany(String manufactureCompany) {
        this.manufactureCompany = manufactureCompany;
    }

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public PlateNumber getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(PlateNumber plateNumber) {
        this.plateNumber = plateNumber;
    }

    public VehicleOwner getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(VehicleOwner currentOwner) {
        this.currentOwner = currentOwner;
    }

    public List<VehicleOwnershipHistory> getOwnershipHistory() {
        return ownershipHistory;
    }

    public void setOwnershipHistory(List<VehicleOwnershipHistory> ownershipHistory) {
        this.ownershipHistory = ownershipHistory;
    }
}