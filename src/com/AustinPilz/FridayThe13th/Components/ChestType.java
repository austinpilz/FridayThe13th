package com.AustinPilz.FridayThe13th.Components;

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
