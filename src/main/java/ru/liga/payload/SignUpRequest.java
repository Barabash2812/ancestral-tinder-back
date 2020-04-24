package ru.liga.payload;

import lombok.Data;
import ru.liga.enums.SexType;

@Data
public class SignUpRequest {

    private String username;

    private String password;

    private String email;

    private String name;

    private String dateOfBirth;

    private SexType sex;

    private String about;


    public SignUpRequest(String username, String password, String email, String name, String dateOfBirth, SexType sex, String about) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.about = about;
    }
}
