package com.AustinPilz.FridayThe13th.Components.Enum;

public enum EscapePointType {
    Land("Land"), Water("Water");

    private final String fieldDescription;

    EscapePointType(String value) {
        fieldDescription = value;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }
}
