package ru.liga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.liga.database.entity.User;
import ru.liga.database.repository.ProfileRepository;
import ru.liga.database.repository.UserRepository;
import ru.liga.exception.ResourceNotFoundException;
import ru.liga.model.dto.UserDTO;
import ru.liga.payload.ApiResponse;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
    }

    public ResponseEntity<ApiResponse> activate(UUID uuid) throws UserPrincipalNotFoundException {
        User userData = userRepository.findUserByActivationCode(uuid).orElseThrow(
                () -> new UserPrincipalNotFoundException("User by activation code not found")
        );
        userData.setActive(true);
        userData.setActivationCode(null);
        userRepository.save(userData);
        return new ResponseEntity<>(new ApiResponse(true, "Email has been confirmed."), HttpStatus.OK);
    }

    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findUserById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    public Long createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        return userRepository.save(user).getId();
    }

    public void deleteUser(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with user_id: " + userId));
        profileRepository.deleteById(user.getProfile().getId());
        userRepository.deleteById(userId);
    }

    public Long updateUser(UserDTO userDTO, Long userId) throws ResourceNotFoundException {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with user_id: " + userId));
        user.setUsername(userDTO.getUsername());
        user.setPassword(encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user).getId();
    }

    private String encode(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }
}
