package rca.rw.secure.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rca.rw.secure.models.User;

import java.util.Optional;

@Repository
public interface IUserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailOrUsername(String email, String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    Optional<User> findByNationalId(String nationalId);

    Optional<User> findByPhone(String phone);
}

