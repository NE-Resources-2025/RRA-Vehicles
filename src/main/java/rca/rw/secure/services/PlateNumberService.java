package rca.rw.secure.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import rca.rw.secure.dtos.plate.CreatePlateNumberDTO;
import rca.rw.secure.dtos.plate.PlateNumberResponseDTO;
import rca.rw.secure.dtos.plate.PlateNumbersResponseDTO;
import rca.rw.secure.dtos.plate.UpdatePlateNumberDTO;
import rca.rw.secure.dtos.response.ApiResponse;

import java.util.List;

public interface PlateNumberService {
    ResponseEntity<ApiResponse<PlateNumberResponseDTO>> registerPlateNumber(CreatePlateNumberDTO request, String username);
    ResponseEntity<ApiResponse<PlateNumberResponseDTO>> updatePlateNumber(Long id, UpdatePlateNumberDTO request, String username);
    ResponseEntity<ApiResponse<Void>> deletePlateNumber(Long id, String username);
    ResponseEntity<ApiResponse<Page<PlateNumberResponseDTO>>> getPlateNumbersByOwner(Long ownerId, Pageable pageable);
    ResponseEntity<ApiResponse<List<PlateNumberResponseDTO>>> getAvailablePlateNumbersByOwner(Long ownerId);
    ResponseEntity<ApiResponse<PlateNumberResponseDTO>> getPlateNumberById(Long id);
    ResponseEntity<ApiResponse<PlateNumberResponseDTO>> getPlateNumberByPlateNumber(String plateNumber);
    ResponseEntity<ApiResponse<Page<PlateNumberResponseDTO>>> getAllPlateNumbers(Pageable pageable);
}