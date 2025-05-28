package com.rra.vehicletracking.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicle_owner")
public class VehicleOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerNames;

    @Column(nullable = false, unique = true)
    private String nationalId;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "vehicleOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlateNumber> plateNumbers = new ArrayList<>();

    public VehicleOwner() {
    }

    public VehicleOwner(Long id, String ownerNames, String nationalId, String phoneNumber,
                        String address, List<PlateNumber> plateNumbers) {
        this.id = id;
        this.ownerNames = ownerNames;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.plateNumbers = plateNumbers;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PlateNumber> getPlateNumbers() {
        return plateNumbers;
    }

    public void setPlateNumbers(List<PlateNumber> plateNumbers) {
        this.plateNumbers = plateNumbers;
    }

    public void addPlateNumber(PlateNumber plateNumber) {
        plateNumbers.add(plateNumber);
        plateNumber.setVehicleOwner(this);
    }

    public void removePlateNumber(PlateNumber plateNumber) {
        plateNumbers.remove(plateNumber);
        plateNumber.setVehicleOwner(null);
    }
}