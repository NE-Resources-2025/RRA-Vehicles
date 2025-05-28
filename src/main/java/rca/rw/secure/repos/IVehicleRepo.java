package com.rra.vehicletracking.repository;

import com.rra.vehicletracking.entity.VehicleOwner;
import com.rra.vehicletracking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>{
    Optional<Vehicle> findByChassisNumber(String chassisNumber);
    Optional<Vehicle> findByPlateNumberPlateNumber(String plateNumber);
    Page<Vehicle> findByCurrentOwner(VehicleOwner owner, Pageable pageable);
    boolean existsByChassisNumber(String chassisNumber);
}