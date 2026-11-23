package com.subastaweb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subastaweb.api.error.ApiError;
import com.subastaweb.domain.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

/** Escribe errores JSON desde los filtros de seguridad (donde no actúa @RestControllerAdvice). */
@Component
public class ErrorResponseWriter {

    private final ObjectMapper objectMapper;

    public ErrorResponseWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void write(HttpServletRequest request, HttpServletResponse response,
                      ErrorCode code, String message) throws IOException {
        response.setStatus(code.status());
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getOutputStream(),
                new ApiError(code.name(), message, RequestIdFilter.currentId(request), List.of()));
    }
}
