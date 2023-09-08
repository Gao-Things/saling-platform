package com.usyd.capstone.common.Enums;

import lombok.Getter;

@Getter
public enum CATEGORY {
    CRYPTOCURRENCY(1, "cryptocurrency"),
    RAREMETAL(2, "raremetal");

    private int value;
    private String name;

    CATEGORY(int value, String name){
        this.value = value;
        this.name = name;
    }

    public static CATEGORY findByValue(int value) {
        switch (value) {
            case 1:
                return CRYPTOCURRENCY;
            case 2:
                return RAREMETAL;

        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
