package com.creativeuncommons.ProgressTrackingSystem.query;

public enum USER_SCHEMA_DATA {
    ID_COLUMN("id"),
    USER_NAME_COLUMN("username"),
    USER_EMAIL_COLUMN("email"),
    USER_PASSWORD_COLUMN("password"),
    SALT_COLUMN("salt");

    private final String name;

    private USER_SCHEMA_DATA(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
