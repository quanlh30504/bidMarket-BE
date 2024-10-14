package com.example.bidMarket.exception;


import com.example.bidMarket.dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class) // bắt all bug
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e){
        ApiResponse response = new ApiResponse();

        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()); //bug
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e){
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse response = new ApiResponse();

        response.setCode(errorCode.getCode()); //bug
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    // Password / Username validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidition(MethodArgumentNotValidException e){
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException exception){

        }

        ApiResponse response = new ApiResponse();

        response.setCode(errorCode.getCode()); //bug
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}
