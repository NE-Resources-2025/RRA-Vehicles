package rca.rw.secure.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.vehicleOwner.CreateVehicleOwnerDTO;
import rca.rw.secure.dtos.vehicleOwner.UpdateVehicleOwnerDTO;
import rca.rw.secure.dtos.vehicleOwner.VehicleOwnerResponseDTO;

public interface VehicleOwnerService {
    ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> registerVehicleOwner(CreateVehicleOwnerDTO request, String username);
    ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> updateVehicleOwner(Long id, UpdateVehicleOwnerDTO request, String username);
    ResponseEntity<ApiResponse<Void>> deleteVehicleOwner(Long id, String username);
    ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> getAllVehicleOwners(Pageable pageable);
    ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> getVehicleOwnerById(Long id);
    ResponseEntity<ApiResponse<VehicleOwnerResponseDTO>> getVehicleOwnerByNationalId(String nationalId);
    ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByNationalId(String nationalId, Pageable pageable);
    ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByPhoneNumber(String phoneNumber, Pageable pageable);
    ResponseEntity<ApiResponse<Page<VehicleOwnerResponseDTO>>> searchVehicleOwnersByEmail(String email, Pageable pageable);
}