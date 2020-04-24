package ru.liga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.liga.database.entity.User;
import ru.liga.database.repository.UserRepository;
import ru.liga.exception.AppException;
import ru.liga.payload.ApiResponse;
import ru.liga.payload.JwtAuthenticationResponse;
import ru.liga.payload.LoginRequest;
import ru.liga.payload.SignUpRequest;
import ru.liga.security.JwtTokenProvider;

import java.net.URI;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MailSenderService senderService;

    @Autowired
    private UserRepository userRepository;

    @Value("${server.address}")
    private String apiHost;

    @Value("${server.port}")
    private String apiPort;

    public ResponseEntity<JwtAuthenticationResponse> authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    public ResponseEntity<ApiResponse> registration(SignUpRequest signUpRequest) {
        if (userRepository.existsUserByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsUserByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest);
        User result = userRepository.save(user);

        try {
            senderService.send(user.getEmail(), "Ancestral Tinder Activation", "Link for activation: http://" + apiHost + ":" + apiPort + "/auth/activation/" + user.getActivationCode());
        } catch (Exception e) {
            throw new AppException("Some problems with sending verification email. Check your email.");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
