package rca.rw.secure.impls;

import rca.rw.secure.dtos.user.*;
import rca.rw.secure.enums.user.EUserRole;
import rca.rw.secure.exceptions.ConflictException;
import rca.rw.secure.exceptions.CustomException;
import rca.rw.secure.exceptions.InternalServerErrorException;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.exceptions.NotFoundException;
import rca.rw.secure.repos.IRoleRepo;
import rca.rw.secure.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rca.rw.secure.enums.user.EUserStatus;
import rca.rw.secure.models.Role;
import rca.rw.secure.models.User;
import rca.rw.secure.repos.IUserRepo;
import rca.rw.secure.utils.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IUserRepo userRepo;
    private final RoleServiceImpl roleService;
    private final IRoleRepo roleRepo;

    @Override
    public User createUserEntity(CreateUserDTO createUserDTO) {
        Optional<User> foundUser = userRepo.findUserByEmailOrUsername(createUserDTO.getEmail(), createUserDTO.getUsername());
        if (foundUser.isPresent())
            throw new ConflictException("The user with the given email or username already exists");
        User user = new User();
        Role role = roleService.getRoleByName(EUserRole.ROLE_STANDARD);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setUsername(createUserDTO.getUsername());
        user.setFullName(createUserDTO.getFirstName() + " " + createUserDTO.getLastName());
        user.setStatus(EUserStatus.ACTIVE);
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(HashUtil.hashPassword(createUserDTO.getPassword()));
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setFullName(createUserDTO.getFirstName() + " " + createUserDTO.getLastName());
        user.setPhone(createUserDTO.getPhone());
        user.setNationalId(createUserDTO.getNationalId());
        user.setRoles(roles);
        return user;
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(CreateUserDTO createUserDTO) {
        try {
            User user = createUserEntity(createUserDTO);
            userRepo.save(user);
            return ApiResponse.success("Successfully created user", HttpStatus.CREATED, new UserResponseDTO(user));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<UsersResponseDTO>> getUsers(Pageable pageable) {
        try {
            Page<User> users = userRepo.findAll(pageable);
            for (User user : users) {
                user.setFullName(user.getFirstName() + " " + user.getLastName());
            }
            return ApiResponse.success("Successfully fetched all users", HttpStatus.OK, new UsersResponseDTO(users));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(Long userId) {
        try {
            User user = findUserById(userId);
            return ApiResponse.success("Successfully fetched user", HttpStatus.OK, new UserResponseDTO(user));
        } catch (Exception e) {
            logger.error("Error fetching user with ID {}: {}", userId, e.getMessage(), e);
            throw new CustomException(e);
        }
    }

    public User findUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new NotFoundException("The Resource was not found"));
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(Long userId, UpdateUserDTO updateUserDTO) {
        try {
            User user = findUserById(userId);
            if (user.getEmail() != null) user.setEmail(updateUserDTO.getEmail());
            if (user.getFirstName() != null) user.setFirstName(updateUserDTO.getFirstName());
            if (user.getLastName() != null) user.setLastName(updateUserDTO.getLastName());
            if (user.getUsername() != null) user.setUsername(updateUserDTO.getUsername());
            return ApiResponse.success("Successfully updated the user", HttpStatus.OK, new UserResponseDTO(user));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public User getLoggedInUser() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new NotFoundException("User Not Found"));
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        return user;
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<UserResponseDTO>> addRoles(Long userId, UserRoleModificationDTO userRoleModificationDTO) {
        try {
            User user = findUserById(userId);
            Set<Role> roles = user.getRoles();
            for (Long roleId : userRoleModificationDTO.getRoles()) {
                Role role = roleRepo.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found"));
                roles.add(role);
            }
            user.setRoles(roles);
            userRepo.save(user);
            return ApiResponse.success("Successfully added roles to the user", HttpStatus.OK, new UserResponseDTO(user));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<UserResponseDTO>> removeRoles(Long userId, UserRoleModificationDTO userRoleModificationDTO) {
        try {
            User user = findUserById(userId);
            Set<Role> roles = user.getRoles();
            for (Long roleId : userRoleModificationDTO.getRoles()) {
                Role role = roleRepo.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found"));
                roles.remove(role);
            }
            user.setRoles(roles);
            userRepo.save(user);
            return ApiResponse.success("Successfully removed roles from the user", HttpStatus.OK, new UserResponseDTO(user));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> deleteUser(Long userId) {
        try {
            User user = findUserById(userId);
            userRepo.deleteById(userId);
            return ApiResponse.success("Successfully deleted the user", HttpStatus.OK, null);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public User getUserByNationalId(String nationalId) {
        return userRepo.findByNationalId(nationalId)
                .orElseThrow(() -> new NotFoundException("User not found with national ID: " + nationalId));
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepo.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("User not found with phone number: " + phone));
    }

}
