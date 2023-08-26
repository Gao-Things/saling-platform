package com.usyd.capstone.common.Enums;

public enum ROLE {
    ROLE_NORMAL("ROLE_NORMAL"),
    ROLE_ADMIN("ROLE_ADMIN"),

    ROLE_SUPER("ROLE_SUPER");

    private final String value;

    ROLE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
