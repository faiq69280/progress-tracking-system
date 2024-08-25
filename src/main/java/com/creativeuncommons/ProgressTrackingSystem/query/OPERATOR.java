package com.creativeuncommons.ProgressTrackingSystem.query;

public enum OPERATOR {
    EQUALS("="),
    GREATER(">"),
    LESSER("<"),
    LESSER_THAN_EQUAL("<="),
    LESSER_THAN_GREATER(">=");

    private final String name;

    private OPERATOR(String s){
        this.name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

}
