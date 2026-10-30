package com.dormi.dormi_be.global.exception;

import com.dormi.dormi_be.global.common.BaseResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusiness(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(errorCode.getStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotReadable(HttpMessageNotReadableException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(errorCode.getStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    // 동시 가입 요청 등으로 사전 중복 검사를 통과해도 DB unique 제약에서 걸리는 경우
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<Void>> handleDataIntegrity(DataIntegrityViolationException e) {
        ErrorCode errorCode = ErrorCode.PHONE_NUMBER_DUPLICATED;
        return ResponseEntity.status(errorCode.getStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }
}
