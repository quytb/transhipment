package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ErrorCode;
import com.havaz.transport.api.common.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LogManager.getLogger(CustomRestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.of(ex.getLocalizedMessage(), ErrorCode.GLOBAL,
                                                       HttpStatus.BAD_REQUEST, errors);
        return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        if (log.isDebugEnabled()) {
            log.debug(ex.getMessage(), ex);
        }

        ErrorResponse errorResponse = ErrorResponse.of(ex.getLocalizedMessage(), ErrorCode.GLOBAL,
                                                       HttpStatus.BAD_REQUEST);

        return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ErrorResponse errorResponse = ErrorResponse.of(ex.getLocalizedMessage(), ErrorCode.GLOBAL,
                                                       HttpStatus.BAD_REQUEST, error);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                               violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ErrorResponse errorResponse =
                ErrorResponse.of(ex.getLocalizedMessage(), ErrorCode.GLOBAL, HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ErrorResponse errorResponse =
                ErrorResponse.of(ex.getLocalizedMessage(), ErrorCode.GLOBAL, HttpStatus.BAD_REQUEST, error);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(ex.getLocalizedMessage(), ErrorCode.GLOBAL,
                                                       HttpStatus.INTERNAL_SERVER_ERROR, "error occurred");
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public final ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
//        log.error(e.getMessage());
//        BindingResult result = e.getBindingResult();
//        List<FieldError> fieldErrors = result.getFieldErrors();
//        if (fieldErrors.size() > 0) {
//            ResultMessage resultMessage = new ResultMessage();
//            for (FieldError fieldError : fieldErrors) {
//                String fieldName = fieldError.getField();
//                String message = fieldError.getDefaultMessage();
//
//                resultMessage.setStatus("Error");
//                resultMessage.setMessage("Error field: "+fieldName+" - message: "+message);
//                //resultMessage.setErrorCode(HttpStatus.BAD_REQUEST);
//            }
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(resultMessage);
//        } else {
//            ResultMessage resultMessage = new ResultMessage();
//            resultMessage.setStatus("Error");
//            resultMessage.setMessage(e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(resultMessage);
//        }
//    }
}
