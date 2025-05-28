package com.rra.vehicletracking.repository;

import com.rra.vehicletracking.entity.VehicleOwnershipHistory;
import com.rra.vehicletracking.entity.Vehicle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface VehicleOwnershipHistoryRepository extends JpaRepository<VehicleOwnershipHistory, Long> {
    List<VehicleOwnershipHistory> findByVehicleOrderByStartDateDesc(Vehicle vehicle);
    Page<VehicleOwnershipHistory> findByVehicle(Vehicle vehicle, Pageable pageable);
    @Query("SELECT h FROM VehicleOwnershipHistory h WHERE h.vehicle.chassisNumber=?1 ORDER BY h.startDate DESC")
    Page<VehicleOwnershipHistory> findByVehicleChassisNumber(String chassisNumber, Pageable pageable);
    @Query("SELECT h FROM VehicleOwnershipHistory h WHERE h.vehicle.plateNumber=?1 ORDER BY h.startDate DESC")
    Page<VehicleOwnershipHistory> findByVehiclePlateNumber(String plateNumber, Pageable pageable);
}
