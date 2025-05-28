package rca.rw.secure.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rca.rw.secure.models.VehicleOwner;

import java.util.Optional;

@Repository
public interface IVehicleOwnerRepo extends JpaRepository<VehicleOwner, Long> {
    Optional<VehicleOwner> findByNationalId(String nationalId);
    Page<VehicleOwner> findByNationalIdContaining(String nationalId, Pageable pageable);
    Page<VehicleOwner> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);
    Page<VehicleOwner> findByUserEmailContaining(String email, Pageable pageable);
    boolean existsByNationalId(String nationalId);
    boolean existsByPhoneNumber(String phoneNumber);
}