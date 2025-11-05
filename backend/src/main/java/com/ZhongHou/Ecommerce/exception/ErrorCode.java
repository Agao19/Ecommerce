package com.ZhongHou.Ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Invalid date of birth", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1010, "Password existed", HttpStatus.BAD_REQUEST),

    TOKEN_EXPIRED(10010, "Token is expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(10011, "Token is invalid", HttpStatus.BAD_REQUEST),


    CRYPTO_EXCEPTION(30150, "The number of crypto co-processors must be between 1 to 16.", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_NOT_EXISTED(1100, "Product not existed", HttpStatus.NOT_FOUND),
    SERVICE_COMPLETED(1101, HttpStatus.ACCEPTED.getReasonPhrase(), HttpStatus.ACCEPTED),
    SERVICE_CANCEL(1102, HttpStatus.PAYMENT_REQUIRED.getReasonPhrase(), HttpStatus.PAYMENT_REQUIRED),


    ;


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
