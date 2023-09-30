package com.usyd.capstone.common.Enums;

public enum PreciousMetalType {
    XAG("silver", "USDXAG"),
    XAU("gold", "USDXAU"),
    XPD("palladium", "USDXPD"),
    XPT("platinum", "USDXPT"),
    XRH("rhodium", "USDXRH");

    private final String explain;

    private final String tranerName;
    PreciousMetalType(String explain, String tranerName) {
        this.explain = explain;
        this.tranerName = tranerName;
    }

    public String getExplain() {
        return explain;
    }

    public String getTranerName(){
        return tranerName;
    }
}
