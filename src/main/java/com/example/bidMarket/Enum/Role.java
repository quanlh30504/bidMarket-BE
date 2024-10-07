package com.example.bidMarket.Enum;

public enum Role {
    ADMIN("ADMIN"),
    BIDDER("BIDDER"),
    SELLER("SELLER");

    private final String value;
    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role getRole(String value) {
        for (Role role : values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Unknown role: " + value);
    }
}