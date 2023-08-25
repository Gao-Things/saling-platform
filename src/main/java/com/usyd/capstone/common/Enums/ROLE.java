package com.usyd.capstone.common.Enums;

public enum ROLE {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String value;

    ROLE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
