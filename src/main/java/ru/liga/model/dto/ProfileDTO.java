package ru.liga.model.dto;

import lombok.Data;
import ru.liga.enums.SexType;

@Data
public class ProfileDTO {

    private String name;

    private String dateOfBirth;

    private SexType sex;

    private String about;

    public ProfileDTO(String name, String dateOfBirth, SexType sex, String about) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.about = about;
    }
}
