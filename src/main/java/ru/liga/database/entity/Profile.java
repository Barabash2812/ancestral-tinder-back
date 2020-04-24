package ru.liga.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.liga.enums.SexType;
import ru.liga.model.dto.ProfileDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "sex")
    @Enumerated(value = EnumType.STRING)
    private SexType sex;

    @Column(name = "about")
    private String about;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "matches",
            joinColumns = {@JoinColumn(name = "lover_id")},
            inverseJoinColumns = {@JoinColumn(name = "belover_id")})
    private Set<Profile> lovers = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "lovers")
    private Set<Profile> belovers = new HashSet<>();

    public Profile(ProfileDTO profileDTO, User user) {
        this.user = user;
        this.name = profileDTO.getName();
        this.sex = profileDTO.getSex();
        this.about = profileDTO.getAbout();
        this.dateOfBirth = profileDTO.getDateOfBirth();
    }

    public Profile(User user) {
        this.user = user;
    }

    public Profile(String name, String dateOfBirth, SexType sex, String about, User user) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.about = about;
        this.user = user;
    }
}
