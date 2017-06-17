package com.AustinPilz.FridayThe13th.Components;

public enum ChestType {

    Weapon("Weapon"), Item("Item");

    private final String fieldDescription;

    private ChestType(String value) {
        fieldDescription = value;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }


}
