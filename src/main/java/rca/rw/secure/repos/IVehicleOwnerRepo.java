package com.rra.vehicletracking.repository;

import com.rra.vehicletracking.entity.VehicleOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, Long>{
    Optional<VehicleOwner> findByNationalId(String nationalId);
    Page<VehicleOwner> findByNationalIdContaining(String nationalId, Pageable pageable);
    Page<VehicleOwner> findByPhoneNumberContaining(String  phoneNumber, Pageable pageable);
    boolean existsByNationalId(String nationalId);
}
