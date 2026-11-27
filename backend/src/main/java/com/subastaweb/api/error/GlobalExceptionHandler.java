package com.subastaweb.api.error;

import com.subastaweb.domain.DomainException;
import com.subastaweb.domain.ErrorCode;
import com.subastaweb.domain.FieldIssue;
import com.subastaweb.security.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/** T1-13: manejo global de errores en Spring Web con el formato del contrato. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    ResponseEntity<ApiError> handleDomain(DomainException ex, HttpServletRequest request) {
        return build(ex.code().status(), ex.code().name(), ex.getMessage(), ex.details(), request);
    }

    @ExceptionHandler(BindException.class)
    ResponseEntity<ApiError> handleValidation(BindException ex, HttpServletRequest request) {
        List<FieldIssue> details = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        String message = fieldError.isBindingFailure()
                                ? "El valor tiene un formato inválido."
                                : fieldError.getDefaultMessage();
                        return new FieldIssue(fieldError.getField(), message == null ? "Valor inválido." : message);
                    }
                    return new FieldIssue(error.getObjectName(),
                            error.getDefaultMessage() == null ? "Valor inválido." : error.getDefaultMessage());
                })
                .sorted(Comparator.comparing(FieldIssue::field).thenComparing(FieldIssue::message))
                .toList();
        return build(ErrorCode.VALIDATION_ERROR.status(), ErrorCode.VALIDATION_ERROR.name(),
                "Los datos enviados no son válidos.", details, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return build(ErrorCode.VALIDATION_ERROR.status(), ErrorCode.VALIDATION_ERROR.name(),
                "El cuerpo de la solicitud no es válido.", List.of(), request);
    }

    /** Un parámetro de consulta con tipo incorrecto es 422; un id de ruta que no es número es 404. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        if (ex.getParameter().hasParameterAnnotation(PathVariable.class)) {
            return build(404, "NOT_FOUND", "El recurso solicitado no existe.", List.of(), request);
        }
        return build(ErrorCode.VALIDATION_ERROR.status(), ErrorCode.VALIDATION_ERROR.name(),
                "Los parámetros de consulta no son válidos.",
                List.of(new FieldIssue(ex.getName(), "El valor tiene un formato inválido.")), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<ApiError> handleTooLarge(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return build(ErrorCode.INVALID_IMAGE.status(), ErrorCode.INVALID_IMAGE.name(),
                "La imagen no es válida.",
                List.of(new FieldIssue("image", "La imagen supera el máximo de 3 MB.")), request);
    }

    /** Errores propios de Spring MVC (404 ruta, 405, 415...) y cualquier fallo inesperado. */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleOthers(Exception ex, HttpServletRequest request) {
        if (ex instanceof ErrorResponse errorResponse) {
            int status = errorResponse.getStatusCode().value();
            HttpStatus resolved = HttpStatus.resolve(status);
            String code = resolved == null ? "REQUEST_ERROR" : resolved.name();
            String message = status == 404 ? "El recurso solicitado no existe." : "La solicitud no es válida.";
            return build(status, code, message, List.of(), request);
        }
        log.error("Error inesperado", ex);
        return build(ErrorCode.INTERNAL_ERROR.status(), ErrorCode.INTERNAL_ERROR.name(),
                "Ocurrió un error inesperado.", List.of(), request);
    }

    private static ResponseEntity<ApiError> build(int status, String code, String message,
                                                  List<FieldIssue> details, HttpServletRequest request) {
        return ResponseEntity.status(status)
                .body(new ApiError(code, message, RequestIdFilter.currentId(request), details));
    }
}
