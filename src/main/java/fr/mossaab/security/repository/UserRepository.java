package fr.mossaab.security.repository;

import fr.mossaab.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByActivationCode(String code);
    Optional<User> findByNickname(String nickname);
}
