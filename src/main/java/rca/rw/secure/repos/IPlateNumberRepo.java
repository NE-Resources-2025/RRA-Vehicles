package com.rra.vehicletracking.repository;

import com.rra.vehicletracking.entity.VehicleOwner;
import com.rra.vehicletracking.entity.PlateNumber;
import com.rra.vehicletracking.entity.PlateNumber.PlateStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlateNumberRepository extends JpaRepository<PlateNumber, Long> {
    Optional<PlateNumber> findByPlateNumber(String plateNumber);
    Page<PlateNumber> findByVehicleOwner(VehicleOwner vehicleOwner, Pageable pageable);
    List<PlateNumber> findByVehicleOwnerAndStatus(VehicleOwner vehicleOwner, PlateStatus status);
    boolean existsByPlateNumber(String plateNumber);
}