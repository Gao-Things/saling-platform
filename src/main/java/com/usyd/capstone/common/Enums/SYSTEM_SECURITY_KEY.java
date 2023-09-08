package com.usyd.capstone.common.Enums;

public enum SYSTEM_SECURITY_KEY {
    PASSWORD_SECRET_KEY("CS76-2"),
    RECAPTCHA_SECRET_KEY("6Lf8Y7onAAAAACsaI8NLwElJ1d_Z9pB9CQGUlEO6"),
    JWT_SECRET_KEY("JWTTestingKeyWithHS256MustHaveASizeGreaterThanOrEqualTo256Bits"),

    CRYPTO_CURRENCY_API_KEY("SR4WYKQHYJC11UF85VX2FRXOWJXM5BTOCJCSTQXS"),

    METALS_API_KEY("97ibnl1idy2mu49i7sjk936cbm58loliz8qcopox6ovf51ykbm1pgni4xa54");


    private final String value;

    SYSTEM_SECURITY_KEY(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
