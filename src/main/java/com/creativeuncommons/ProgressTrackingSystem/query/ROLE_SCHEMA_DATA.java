package com.creativeuncommons.ProgressTrackingSystem.query;

public enum ROLE_SCHEMA_DATA {
    ROLE_NAME("role_name"),
    ROLE_UUID("role_uuid");

    String name;

    private ROLE_SCHEMA_DATA(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
