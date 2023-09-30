package com.usyd.capstone.common.Enums;

import lombok.Getter;

@Getter
public enum CATEGORY {
    UNDEFINED(0,"undefined"),
    CRYPTOCURRENCY(1, "cryptocurrency"),
    PRECIOUS_METAL(2, "precious metals"); //贵金属，之前的命名有误

    private int value;
    private String name;

    CATEGORY(int value, String name){
        this.value = value;
        this.name = name;
    }

    public static CATEGORY findByValue(int value) {
        switch (value) {
            case 0:
                return UNDEFINED;
            case 1:
                return CRYPTOCURRENCY;
            case 2:
                return PRECIOUS_METAL;

        }
        return null;
    }

    public static CATEGORY findByName(String name) {
        switch (name) {
            case "undefined":
                return UNDEFINED;
            case "cryptocurrency":
                return CRYPTOCURRENCY;
            case "rare metal":
                return PRECIOUS_METAL;

        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
