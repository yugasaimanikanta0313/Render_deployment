package klu.repository;


import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import klu.entitiy.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    void deleteByOtpGeneratedTimeBefore(LocalDateTime thresholdTime);
    Optional<User> findById(Integer id);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByResetToken(String resetToken);
    boolean existsByEmail(String email);
}