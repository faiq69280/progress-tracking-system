package com.creativeuncommons.ProgressTrackingSystem.query;

public enum LINK_USER_ROLE_SCHEMA_DATA {
    ROLE_UUID("role_uuid"),
    USER_UUID("user_uuid");

    private String name;

    private LINK_USER_ROLE_SCHEMA_DATA(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }


}
