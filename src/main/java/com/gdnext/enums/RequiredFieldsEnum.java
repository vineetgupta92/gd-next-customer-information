package com.gdnext.enums;

public enum RequiredFieldsEnum {
    BIRTHDATE("birthdate"),
    BIRTHPLACE("birthplace"),
    SEX("sex"),
    ADDRESS("address");

    private final String fieldName;

    RequiredFieldsEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
