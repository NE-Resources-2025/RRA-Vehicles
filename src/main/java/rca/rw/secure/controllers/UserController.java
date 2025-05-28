package rca.rw.secure.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.user.*;
import rca.rw.secure.models.User;
import rca.rw.secure.services.UserService;
import rca.rw.secure.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class UserController {
    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@RequestBody CreateUserDTO createUserDTO) {
        return this.userService.createUser(createUserDTO);
    }

    @GetMapping("/get-users")
    public ResponseEntity<ApiResponse<UsersResponseDTO>> getUsers(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return userService.getUsers(pageable);
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping("/update-user/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable Long userId, @RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(userId, updateUserDTO);
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

//    Patch cause it's an incremental update, not a replacement
    @PatchMapping("/add-roles/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> addRoles(@Valid @PathVariable("userId") Long userId, @Valid @RequestBody UserRoleModificationDTO userRoleModificationDTO) {
        return userService.addRoles(userId, userRoleModificationDTO);
    }

    @PatchMapping("/remove-roles/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> removeRoles(@Valid @PathVariable("userId") Long userId, @Valid @RequestBody UserRoleModificationDTO userRoleModificationDTO) {
        return userService.removeRoles(userId, userRoleModificationDTO);
    }
}
