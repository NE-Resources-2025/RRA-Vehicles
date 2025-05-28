package rca.rw.secure.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.vehicleOwnerHistory.CreateVehicleOwnershipHistoryDTO;
import rca.rw.secure.dtos.vehicleOwnerHistory.UpdateVehicleOwnershipHistoryDTO;
import rca.rw.secure.dtos.vehicleOwnerHistory.VehicleOwnershipHistoryResponseDTO;

public interface VehicleOwnershipHistoryService {
    ResponseEntity<ApiResponse<VehicleOwnershipHistoryResponseDTO>> createOwnershipHistory(CreateVehicleOwnershipHistoryDTO request, String username);
    ResponseEntity<ApiResponse<VehicleOwnershipHistoryResponseDTO>> updateOwnershipHistory(Long id, UpdateVehicleOwnershipHistoryDTO request, String username);
    ResponseEntity<ApiResponse<Void>> deleteOwnershipHistory(Long id, String username);
    ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByVehicleId(Long vehicleId, Pageable pageable);
    ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByChassisNumber(String chassisNumber, Pageable pageable);
    ResponseEntity<ApiResponse<Page<VehicleOwnershipHistoryResponseDTO>>> getHistoryByPlateNumber(String plateNumber, Pageable pageable);
}