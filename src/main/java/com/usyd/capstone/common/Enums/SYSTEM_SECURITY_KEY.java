package com.usyd.capstone.common.Enums;

public enum SYSTEM_SECURITY_KEY {
    PASSWORD_SECRET_KEY("CS76-2"),
    RECAPTCHA_SECRET_KEY("6Lf8Y7onAAAAACsaI8NLwElJ1d_Z9pB9CQGUlEO6"),
    JWT_SECRET_KEY("JWTTestingKeyWithHS256MustHaveASizeGreaterThanOrEqualTo256Bits");
    

    private final String value;

    SYSTEM_SECURITY_KEY(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
