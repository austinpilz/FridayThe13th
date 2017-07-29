package com.AustinPilz.FridayThe13th.Components.Enum;

public enum GameStatus {

    Empty("Empty"), Waiting("Waiting"), InProgress("In Progress");

    private final String fieldDescription;

    GameStatus(String value) {
        fieldDescription = value;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }


}
