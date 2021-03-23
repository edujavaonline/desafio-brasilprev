package com.brazilprev.brazilprev.exception.exceptionhandler;

import com.brazilprev.brazilprev.exception.BusinessException;
import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorType errorType = ApiErrorType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .userDetail(detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorType errorType = ApiErrorType.ERROR_BUSINESS;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .userDetail(detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof UnrecognizedPropertyException) {
            return handleUnrecognizedPropertyException((UnrecognizedPropertyException) rootCause, headers, status, request);
        }

        ApiErrorType errorType = ApiErrorType.MESSAGE_NOT_READBLE;
        String detail = messageSource.getMessage("message.not.readable.generic", null, LocaleContextHolder.getLocale());

        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .userDetail(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()))
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorType errorType = ApiErrorType.RESOURCE_NOT_FOUND;
        String detail = String.format(messageSource.getMessage("invalid.url.resource", null, LocaleContextHolder.getLocale()), ex.getRequestURL());

        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .userDetail(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()))
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidatorInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        if (body == null) {
            body = ApiError.builder()
                    .timeStamp(OffsetDateTime.now())
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userDetail(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()))
                    .build();
        } else if (body instanceof String) {
            body = ApiError.builder()
                    .timeStamp(OffsetDateTime.now())
                    .title((String) body)
                    .status(status.value())
                    .userDetail(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()))
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> handleValidatorInternal(Exception ex, BindingResult bindingResult,
                                                           HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<ApiError.Field> fields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                    return ApiError.Field.builder()
                            .name(fieldError.getField())
                            .userMessage(message).build();
                })
                .collect(Collectors.toList());

        ApiErrorType errorType = ApiErrorType.INVALID_DATA;
        String detail = String.format(messageSource.getMessage("invalid.data", null, LocaleContextHolder.getLocale()));
        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .fields(fields)
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorType errorType = ApiErrorType.INVALID_URL_PARAMETER;
        String detail = String.format(messageSource.getMessage("invalid.url.parameter", null, LocaleContextHolder.getLocale()),
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .userDetail(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()))
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }



    private ResponseEntity<Object> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorType errorType = ApiErrorType.INVALID_PAYLOAD_PARAMETER;
        String detail = String.format(messageSource.getMessage("property.not.exist", null, LocaleContextHolder.getLocale()), ex.getPropertyName());

        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .userDetail(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()))
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        ApiErrorType  errorType = ApiErrorType.INVALID_PAYLOAD_PARAMETER;
        String detail = String.format(messageSource.getMessage("invalid.payload", null, LocaleContextHolder.getLocale()), path, ex.getValue(), ex.getTargetType().getSimpleName());

        ApiError apiError = createApiErrorBuilder(status, errorType, detail)
                .userDetail(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()))
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    private ApiError.ApiErrorBuilder createApiErrorBuilder(HttpStatus status, ApiErrorType errorType, String detail) {
        return ApiError.builder()
                .timeStamp(OffsetDateTime.now())
                .detail(detail)
                .status(status.value())
                .title(errorType.getTitle())
                .type(errorType.getUri())
                .userDetail(detail);
    }
}
