package com.example.bidMarket.exception;

public enum ErrorCode {
    // User-related errors (1xxx)
    USER_EXISTED(1001, "User already exists"),
    USER_NOT_FOUND(1002, "User not found"),
    USERNAME_INVALID(1003, "Username must be at least 5 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters"),
    EMAIL_INVALID(1005, "Email is invalid"),
    USER_UNAUTHORIZED(1006, "User unauthorized"),
    USER_ACCOUNT_LOCKED(1007, "User account is locked"),
    USER_ROLE_INVALID(1008, "Invalid user role"),

    // Validation errors (2xxx)
    INVALID_REQUEST(2001, "Invalid request format"),
    FIELD_MISSING(2002, "Required field is missing"),
    INVALID_FIELD_FORMAT(2003, "Field format is invalid"),
    DATA_INTEGRITY_VIOLATION(2004, "Data integrity violation"),
    INVALID_KEY(2005, "Invalid key provided"),

    // Authentication/Authorization errors (3xxx)
    AUTHENTICATION_FAILED(3001, "Authentication failed"),
    TOKEN_EXPIRED(3002, "Token has expired"),
    TOKEN_INVALID(3003, "Invalid token"),
    ACCESS_DENIED(3004, "Access denied"),

    // Resource-related errors (4xxx)
    RESOURCE_NOT_FOUND(4001, "Requested resource not found"),
    RESOURCE_CONFLICT(4002, "Resource conflict"),
    RESOURCE_CREATION_FAILED(4003, "Failed to create resource"),
    RESOURCE_UPDATE_FAILED(4004, "Failed to update resource"),
    RESOURCE_DELETION_FAILED(4005, "Failed to delete resource"),

    // Auction-related errors (5xxx)
    AUCTION_NOT_FOUND(5001, "Auction not found"),
    AUCTION_ALREADY_CLOSED(5002, "Auction has already closed"),
    AUCTION_CREATION_FAILED(5003, "Failed to create auction"),
    AUCTION_BID_REJECTED(5004, "Bid rejected due to auction rules"),
    AUCTION_INVALID_STATUS(5005, "Auction status is invalid"),
    AUCTION_UNAUTHORIZED_ACCESS(5006, "Unauthorized access to the auction"),
    AUCTION_UPDATE_FAILED(5006, "Failed to update auction"),
    AUCTION_DELETE_FAILED(5007, "Failed to delete auction"),
    AUCTION_OPEN_FAILED(5008, "Failed to open auction"),
    AUCTION_CLOSE_FAILED(5009, "Failed to close auction"),
    AUCTION_COMPLETE_FAILED(5010, "Failed to complete auction"),
    AUCTION_CANCEL_FAILED(5011, "Failed to cancel auction"),
    AUCTION_REOPEN_FAILED(5012, "Failed to reopen auction"),

    // Bid-related errors (6xxx)
    BID_NOT_FOUND(6001, "Bid not found"),
    BID_TOO_LOW(6002, "Bid is lower than the current highest bid"),
    BID_ALREADY_PLACED(6003, "Bid has already been placed"),
    BID_CREATION_FAILED(6004, "Failed to place bid"),
    BID_UPDATE_FAILED(6005, "Failed to update bid"),
    BID_REJECTED_DUE_TO_CLOSURE(6006, "Bid rejected because the auction is closed"),

    // Profile-related errors (7xxx)
    PROFILE_NOT_FOUND(7001, "Profile not found"),
    PROFILE_UPDATE_FAILED(7002, "Failed to update profile"),
    PROFILE_IMAGE_UPLOAD_FAILED(7003, "Failed to upload profile image"),
    PROFILE_IMAGE_NOT_FOUND(7004, "Profile image not found"),

    // Product-related errors (8xxx)
    PRODUCT_NOT_FOUND(8001, "Product not found"),
    PRODUCT_CREATION_FAILED(8002, "Failed to create product"),
    PRODUCT_UPDATE_FAILED(8003, "Failed to update product"),
    PRODUCT_DELETION_FAILED(8004, "Failed to delete product"),
    PRODUCT_OUT_OF_STOCK(8005, "Product is out of stock"),
    PRODUCT_ALREADY_LISTED(8006, "Product is already listed for auction"),

    // System errors (9xxx)
    SYSTEM_ERROR(9001, "System error occurred"),
    DATABASE_ERROR(9002, "Database error"),
    NETWORK_ERROR(9003, "Network error"),
    SERVICE_UNAVAILABLE(9004, "Service unavailable"),
    TIMEOUT_ERROR(9005, "Request timed out"),

    // File-related errors (10xxx)
    FILE_UPLOAD_FAILED(10001, "File upload failed"),
    FILE_NOT_FOUND(10002, "File not found"),
    FILE_FORMAT_UNSUPPORTED(10003, "Unsupported file format"),
    FILE_TOO_LARGE(10004, "File size exceeds the limit"),
    FILE_DELETE_FAILED(10005, "Failed to delete file"),
    // Payment/Transaction errors (11xxx)
    PAYMENT_FAILED(11001, "Payment processing failed"),
    INSUFFICIENT_FUNDS(11002, "Insufficient funds"),
    TRANSACTION_ERROR(11003, "Transaction error"),
    PAYMENT_METHOD_INVALID(11004, "Invalid payment method"),

    // Category
    CATEGORY_NOT_EXISTED(12001,"Category not existed"),
    // Uncategorized errors (9999)
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error");


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
