package com.example.bidMarket.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User existed"),
    INVALID_KEY(1001, "Invalid message key"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    USERNAME_INVALID(1002, "Username must be at least 5 characters"),
    PASSWORD_INVALID(1003, "Password must be at least 8 characters"),
    EMAIL_INVALID(1004, "Email is invalid"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
