package com.subastaweb.api.error;

import com.subastaweb.domain.FieldIssue;
import java.util.List;

/** Formato único de error: code, message, requestId y details (sin revelar secretos). */
public record ApiError(String code, String message, String requestId, List<FieldIssue> details) {
}
