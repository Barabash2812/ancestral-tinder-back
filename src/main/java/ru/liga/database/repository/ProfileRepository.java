package ru.liga.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.database.entity.Profile;
import ru.liga.database.entity.User;
import ru.liga.enums.SexType;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByUser(User user);

    Optional<Profile> findProfileById(Long profileId);

    Optional<Set<Profile>> findProfilesBySexAndBeloversIsNotContaining(SexType sex, Profile profile);
}
