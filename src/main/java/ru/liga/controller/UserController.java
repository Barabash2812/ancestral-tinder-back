package ru.liga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.liga.database.entity.User;
import ru.liga.exception.ResourceNotFoundException;
import ru.liga.model.dto.UserDTO;
import ru.liga.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping()
    public ResponseEntity<Long> createUser(@RequestBody UserDTO userDTO) {
        Long userId = userService.createUser(userDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/{userId}")
                .build(userId);
        return ResponseEntity.created(uri).body(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Long> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.updateUser(userDTO, userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) throws ResourceNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("User deleted");
    }
}
