package ru.liga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.liga.database.entity.Profile;
import ru.liga.database.entity.User;
import ru.liga.exception.ResourceNotFoundException;
import ru.liga.model.dto.ProfileDTO;
import ru.liga.service.ProfileService;

import java.util.Set;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping()
    public ResponseEntity<Long> updateProfile(@RequestBody ProfileDTO profileDTO,
                                              @AuthenticationPrincipal User principal) throws ResourceNotFoundException {
        Long id = profileService.updateProfile(profileDTO, principal.getId());
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long profileId) throws ResourceNotFoundException {
        Profile profile = profileService.getProfileById(profileId);
        return ResponseEntity.ok().body(profile);
    }

    @GetMapping("/next")
    public ResponseEntity<Profile> getNextProfile() throws ResourceNotFoundException {
        Profile profile = profileService.getNextProfile();
        return ResponseEntity.ok().body(profile);
    }

    @PostMapping("/init")
    public ResponseEntity initProfileSupplier() throws ResourceNotFoundException {
        profileService.initProfileSupplier();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like")
    public boolean addLover(@RequestBody Profile whom,
                            @AuthenticationPrincipal User who) throws ResourceNotFoundException {
        return profileService.addLover(who.getProfile().getId(), whom.getId());
    }

    @GetMapping("/lovers")
    public ResponseEntity<Set<Profile>> getLoversByProfileId(@AuthenticationPrincipal User principal) throws ResourceNotFoundException {
        Long profileId = principal.getProfile().getId();
        return ResponseEntity.ok().body(profileService.getLovers(profileId));
    }

    @DeleteMapping("/lovers/{loverId}")
    public ResponseEntity removeLoverById(@AuthenticationPrincipal User principal,
                                          @PathVariable Long loverId) throws ResourceNotFoundException {
        Long profileId = principal.getProfile().getId();
        profileService.removeLoverById(profileId, loverId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lovers/{loverId}/check")
    public boolean checkMatch(@AuthenticationPrincipal User principal,
                              @PathVariable Long loverId) throws ResourceNotFoundException {
        Long profileId = principal.getProfile().getId();
        return profileService.checkMatch(profileId, loverId);
    }
}
