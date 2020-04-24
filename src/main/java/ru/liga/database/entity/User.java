package ru.liga.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.liga.model.dto.UserDTO;
import ru.liga.payload.SignUpRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usrs")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "email")
    private String email;

    @Column
    private UUID activationCode;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;

    public User(UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.password = encode(userDTO.getPassword());
        this.activationCode = UUID.randomUUID();
        this.isActive = false;
        this.email = userDTO.getEmail();
        this.profile = new Profile(this);
    }

    public User(SignUpRequest signUpRequest) {
        this.username = signUpRequest.getUsername();
        this.password = encode(signUpRequest.getPassword());
        this.activationCode = UUID.randomUUID();
        this.isActive = false;
        this.email = signUpRequest.getEmail();
        this.profile = new Profile(signUpRequest.getName(), signUpRequest.getDateOfBirth(), signUpRequest.getSex(), signUpRequest.getAbout(), this);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority("USER")));
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    private String encode(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }
}
