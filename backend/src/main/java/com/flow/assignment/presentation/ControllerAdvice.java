package com.flow.assignment.presentation;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.flow.assignment.dto.response.ErrorResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    private static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_CODE_DELIMITER = "\\|\\|";
    private static final int METHOD_ARGUMENT_NOT_VALID_EXCEPTION_ERROR_INDEX = 0;
    private static final int METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_INDEX = 1;

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(final InvalidFormatException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("REQUEST_FORMAT_001", "허용 시간 입력은 yyyy/MM/dd HH/mm 의 형식이어야 합니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        FieldError firstFieldError = fieldErrors.get(0);
        String[] errorCodeAndMessage = firstFieldError.getDefaultMessage()
                .split(METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_CODE_DELIMITER);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(errorCodeAndMessage[METHOD_ARGUMENT_NOT_VALID_EXCEPTION_ERROR_INDEX],
                        errorCodeAndMessage[METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_INDEX]));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        String[] errorCodeAndMessage = e.getMessage().split(METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_CODE_DELIMITER);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(errorCodeAndMessage[METHOD_ARGUMENT_NOT_VALID_EXCEPTION_ERROR_INDEX],
                        errorCodeAndMessage[METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_INDEX]));
    }
}
