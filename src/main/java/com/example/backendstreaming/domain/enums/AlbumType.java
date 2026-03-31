package com.example.backendstreaming.domain.enums;

public enum AlbumType {

    ALBUM("Album"),
    SINGLE("Single"),
    EP("EP");

    private final String displayName;

    AlbumType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
