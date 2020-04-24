package ru.liga.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SexType {
    FEMALE("female"),
    MALE("male");

    private final String sex;

    SexType(String sex) {
        this.sex = sex;
    }

    public static SexType convert(String sex) {
        switch (sex) {
            case "male":
                return SexType.MALE;
            case "female":
                return SexType.FEMALE;
            default:
                throw new RuntimeException("No such type");
        }
    }

    public static SexType getRevertSex(SexType sexType) {
        switch (sexType) {
            case FEMALE:
                return SexType.MALE;
            case MALE:
                return SexType.FEMALE;
            default:
                throw new IllegalStateException("Unexpected value: " + sexType);
        }
    }

    @JsonValue
    public String getSex() {
        return sex;
    }
}
