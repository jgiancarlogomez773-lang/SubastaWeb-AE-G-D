package com.subastaweb.domain;

import java.util.List;

public class DomainException extends RuntimeException {

    private final ErrorCode code;
    private final List<FieldIssue> details;

    public DomainException(ErrorCode code, String message) {
        this(code, message, List.of());
    }

    public DomainException(ErrorCode code, String message, List<FieldIssue> details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    public ErrorCode code() {
        return code;
    }

    public List<FieldIssue> details() {
        return details;
    }
}
