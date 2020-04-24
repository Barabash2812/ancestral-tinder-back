package ru.liga.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.database.entity.User;
import ru.liga.exception.ResourceNotFoundException;
import ru.liga.payload.ApiResponse;
import ru.liga.payload.JwtAuthenticationResponse;
import ru.liga.payload.LoginRequest;
import ru.liga.payload.SignUpRequest;
import ru.liga.security.JwtTokenProvider;
import ru.liga.service.AuthenticationService;
import ru.liga.service.UserService;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registration(signUpRequest);
    }

    @GetMapping("/activation/{uuid}")
    public ResponseEntity<ApiResponse> activation(@PathVariable UUID uuid) throws UserPrincipalNotFoundException {
        return userService.activate(uuid);
    }

    @GetMapping("/current/{token}")
    public User getCurrentUser(@PathVariable String token) throws ResourceNotFoundException {
        return userService.getUserById(tokenProvider.getUserIdFromJWT(token));
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestBody String token) {
        JSONObject tokenJson = new JSONObject(token);
        return tokenProvider.validateToken(tokenJson.get("value").toString());
    }
}
