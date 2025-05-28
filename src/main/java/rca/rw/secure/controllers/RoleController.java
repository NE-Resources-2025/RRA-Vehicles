package rca.rw.secure.controllers;

import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.role.CreateRoleDTO;
import rca.rw.secure.dtos.role.RolesResponseDTO;
import rca.rw.secure.utils.Constants;
import rca.rw.secure.dtos.role.RoleResponseDTO;
import rca.rw.secure.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create-role")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) {
        return roleService.createRole(createRoleDTO);
    }

    @GetMapping("/get-roles")
    public ResponseEntity<ApiResponse<RolesResponseDTO>> getAllRoles(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return roleService.getRoles(pageable);
    }
}
