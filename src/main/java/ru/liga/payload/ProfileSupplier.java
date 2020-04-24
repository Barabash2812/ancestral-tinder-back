package ru.liga.payload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.liga.database.entity.Profile;
import ru.liga.database.entity.User;
import ru.liga.exception.ResourceNotFoundException;
import ru.liga.service.ProfileService;

import java.util.Iterator;

@Component
public class ProfileSupplier {

    @Autowired
    private ProfileService profileService;

    private Iterator<Profile> iterator;

    public ProfileSupplier() {
    }

    public Profile next() throws ResourceNotFoundException {
        if (!iterator.hasNext()) {
            if (init()) {
                return profileService.getProfileById(iterator.next().getId());
            }
            return null;
        }
        return profileService.getProfileById(iterator.next().getId());
    }

    public boolean init() throws ResourceNotFoundException {
        Profile forWhom = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        if (!this.profileService.getAvailableProfiles(forWhom.getUser().getId()).isEmpty()) {
            this.iterator = this.profileService.getAvailableProfiles(forWhom.getUser().getId()).iterator();
            return true;
        }
        return false;
    }
}
