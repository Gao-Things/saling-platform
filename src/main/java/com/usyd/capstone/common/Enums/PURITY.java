package com.usyd.capstone.common.Enums;

import lombok.Getter;

import java.security.Key;

@Getter
public enum PURITY {
    GOLD_24K(0, "24K"),
    GOLD_22K(1, "22K"),
    GOLD_21K(2, "21K"),
    GOLD_18K(3, "18K"),
    GOLD_14K(4, "14K"),
    GOLD_12K(5, "12K"),
    GOLD_10K(6, "10K"),
    SILVER_999(7, "999"),
    SILVER_995(8, "995"),
    SILVER_925(9, "925"),
    SILVER_990(10, "990");

    private int value;
    private String name;

    PURITY(int value, String name){
        this.value = value;
        this.name = name;
    }
    public static PURITY findByValue(int value) {
        for (PURITY purity : values()) {
            if (purity.value == value) {
                return purity;
            }
        }
        return null;
    }

    public static PURITY findByName(String name) {
        for (PURITY purity : values()) {
            if (purity.name.equals(name)) {
                return purity;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
