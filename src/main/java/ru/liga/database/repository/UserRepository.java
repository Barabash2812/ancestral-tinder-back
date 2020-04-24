package ru.liga.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.database.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long id);

    Optional<User> findUserByActivationCode(UUID uuid);

    Optional<User> findUserByUsername(String username);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);
}
