package com.coyotwilly.casso.enums;

public enum LockType {
    DEVICE("device"),
    USER("user");

    private final String type;

    LockType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static LockType fromString(String text) {
        for (LockType lock : LockType.values()) {
            if (lock.type.equalsIgnoreCase(text)) {
                return lock;
            }
        }

        throw new IllegalArgumentException("Lock with type " + text + " does not exist");
    }
}
