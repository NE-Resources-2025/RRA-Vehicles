package rca.rw.secure.repos;

import rca.rw.secure.enums.vehicle.EPlateStatus;
import rca.rw.secure.models.PlateNumber;
import rca.rw.secure.models.VehicleOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPlateNumberRepo extends JpaRepository<PlateNumber, Long> {
    Optional<PlateNumber> findByPlateNumber(String plateNumber);
    Page<PlateNumber> findByVehicleOwner(VehicleOwner vehicleOwner, Pageable pageable);
    List<PlateNumber> findByVehicleOwnerAndStatus(VehicleOwner vehicleOwner, EPlateStatus status);
    boolean existsByPlateNumber(String plateNumber);
}