package rca.rw.secure.repos;

import rca.rw.secure.models.Vehicle;
import rca.rw.secure.models.VehicleOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVehicleRepo extends JpaRepository<Vehicle, Long>{
    Optional<Vehicle> findByChassisNumber(String chassisNumber);
    Optional<Vehicle> findByPlateNumberPlateNumber(String plateNumber);
    Page<Vehicle> findByCurrentOwner(VehicleOwner owner, Pageable pageable);
    boolean existsByChassisNumber(String chassisNumber);
}