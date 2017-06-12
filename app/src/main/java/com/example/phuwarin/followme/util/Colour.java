package com.example.phuwarin.followme.util;

/**
 * Created by Phuwarin on 6/12/2017.
 */

public enum Colour {
    RED("#f44336"), PINK("#e91e63"), PURPLE("#9c27b0"),
    DEEP_PURPLE("#673ab7"), INDIGO("#3f51b5"), BLUE("#2196f3"),
    LIGHT_BLUE("#03a9f4"), CYAN("#00bcd4"), TEAL("#009688"),
    GREEN("#4caf50"), LIGHT_GREEN("#8bc34a"), LIME("#cddc39"),
    YELLOW("#ffeb3b"), AMBER("#ffc107"), ORANGE("#ff9800"),
    DEEP_ORANGE("#ff5722"), BROWN("#795548"), GREY("#9e9e9e"),
    BLUE_GREY("#607d8b"), BLACK("#000000"), WHITE("#ffffff");

    private final String code;

    Colour(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
