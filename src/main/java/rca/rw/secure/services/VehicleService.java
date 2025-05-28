package rca.rw.secure.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.vehicle.CreateVehicleDTO;
import rca.rw.secure.dtos.vehicle.UpdateVehicleDTO;
import rca.rw.secure.dtos.vehicle.VehicleResponseDTO;
import rca.rw.secure.dtos.vehicle.VehicleTransferDTO;

public interface VehicleService {
    ResponseEntity<ApiResponse<VehicleResponseDTO>> registerVehicle(CreateVehicleDTO request, String username);
    ResponseEntity<ApiResponse<VehicleResponseDTO>> updateVehicle(Long id, UpdateVehicleDTO request, String username);
    ResponseEntity<ApiResponse<Void>> deleteVehicle(Long id, String username);
    ResponseEntity<ApiResponse<VehicleResponseDTO>> transferVehicle(VehicleTransferDTO request, String username);
    ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleById(Long id);
    ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleByChassisNumber(String chassisNumber);
    ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleByPlateNumber(String plateNumber);
    ResponseEntity<ApiResponse<Page<VehicleResponseDTO>>> getVehiclesByOwner(Long ownerId, Pageable pageable);
}