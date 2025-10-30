package com.ZhongHou.Ecommerce.exception;
import com.ZhongHou.Ecommerce.dto.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    //handle các  exception khác, hiện log ra trong console
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ExceptionResponse> handlingRuntimeException(Exception exception){
        log.error("Exception: ", exception);
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        exceptionResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    //Handle App
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ExceptionResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setCode(errorCode.getCode());
        exceptionResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(exceptionResponse);
    }

    //Handle AUTHORIZED
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ExceptionResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ExceptionResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    //Check validation in controller
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ExceptionResponse> handlingValidation(MethodArgumentNotValidException exception){

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        String enumKey = exception.getFieldError().getDefaultMessage();

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e){

        }

        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setCode(errorCode.getCode());
        exceptionResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    //Check logic business
    @ExceptionHandler(value = BusinessException.class)
    ResponseEntity<ExceptionResponse> handlingBusinessException(BusinessException exception){
        log.error("Exception: ", exception);

        ExceptionResponse exResponse = new ExceptionResponse();

        exResponse.setCode(ErrorCode.CRYPTO_EXCEPTION.getCode());
        exResponse.setMessage(ErrorCode.CRYPTO_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(exResponse);
    }
}
