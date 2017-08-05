package com.AustinPilz.FridayThe13th.Components.Enum;

public enum ChestType {

    Weapon("Weapon"), Item("Item");

    private final String fieldDescription;

    ChestType(String value) {
        fieldDescription = value;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }


}
