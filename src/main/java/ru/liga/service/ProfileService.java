package ru.liga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.database.entity.Profile;
import ru.liga.database.entity.User;
import ru.liga.database.repository.ProfileRepository;
import ru.liga.database.repository.UserRepository;
import ru.liga.enums.SexType;
import ru.liga.exception.ResourceNotFoundException;
import ru.liga.model.dto.ProfileDTO;
import ru.liga.payload.ProfileSupplier;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProfileService {

    @Autowired
    private ProfileSupplier profileSupplier;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public Long updateProfile(ProfileDTO profileDTO, Long userId) throws ResourceNotFoundException {
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with user_id " + userId + " not found"));
        Profile profile = profileRepository.findProfileByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("Profile with user_id " + userId + " not found")
        );
        profile.setAbout(profileDTO.getAbout());
        profile.setDateOfBirth(profileDTO.getDateOfBirth());
        profile.setSex(profileDTO.getSex());
        profile.setName(profileDTO.getName());
        profileRepository.save(profile);
        return profile.getId();
    }

    public Long createProfile(ProfileDTO profileDTO, User user) {
        Profile profile = new Profile(profileDTO, user);
        return profileRepository.save(profile).getId();
    }

    public Profile getProfileById(Long profileId) throws ResourceNotFoundException {
        return profileRepository.findProfileById(profileId).orElseThrow(
                () -> new ResourceNotFoundException("Profile with profile_id " + profileId + " not found")
        );
    }

    public Set<Profile> getAvailableProfiles(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with user_id " + userId + " not found"));


        Profile profile = profileRepository.findProfileByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("Profile for user_id=" + userId + " not found")
        );

        return profileRepository.findProfilesBySexAndBeloversIsNotContaining(
                SexType.getRevertSex(user.getProfile().getSex()), profile)
                .orElse(new HashSet<>());
    }

    public boolean addLover(Long whoId, Long whomId) throws ResourceNotFoundException {
        Profile who = profileRepository.findProfileById(whoId).orElseThrow(() ->
                new ResourceNotFoundException("Profile with id=(" + whoId + ") not found")
        );
        Profile whom = profileRepository.findProfileById(whomId).orElseThrow(() ->
                new ResourceNotFoundException("Profile with id=(" + whomId + ") not found")
        );
        who.getLovers().add(whom);
        whom.getBelovers().add(who);
        profileRepository.save(who);
        profileRepository.save(whom);
        return whom.getLovers().contains(who);
    }

    public void initProfileSupplier() throws ResourceNotFoundException {
        profileSupplier.init();
    }

    public Profile getNextProfile() throws ResourceNotFoundException {
        return profileSupplier.next();
    }

    public Set<Profile> getLovers(Long id) throws ResourceNotFoundException {
        Profile profile = profileRepository.findProfileById(id).orElseThrow(() ->
                new ResourceNotFoundException("Profile with id=(" + id + ") not found")
        );
        return profile.getLovers();
    }

    public void removeLoverById(Long whoId, Long whomId) throws ResourceNotFoundException {
        Profile who = profileRepository.findProfileById(whoId).orElseThrow(() ->
                new ResourceNotFoundException("Profile with id=(" + whoId + ") not found")
        );
        Profile whom = profileRepository.findProfileById(whomId).orElseThrow(() ->
                new ResourceNotFoundException("Profile with id=(" + whomId + ") not found")
        );
        who.getLovers().remove(whom);
        whom.getBelovers().remove(who);
        profileRepository.save(who);
        profileRepository.save(whom);
    }

    public boolean checkMatch(Long whoId, Long whomId) throws ResourceNotFoundException {
        Profile who = profileRepository.findProfileById(whoId).orElseThrow(() ->
                new ResourceNotFoundException("Profile with id=(" + whoId + ") not found")
        );
        Profile whom = profileRepository.findProfileById(whomId).orElseThrow(() ->
                new ResourceNotFoundException("Profile with id=(" + whomId + ") not found")
        );
        return who.getLovers().contains(whom) && whom.getLovers().contains(who);
    }
}
