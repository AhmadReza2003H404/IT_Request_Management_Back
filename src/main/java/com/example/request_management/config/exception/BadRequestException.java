package com.example.request_management.config.exception;

public class BadRequestException extends BusinessException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }

    public BadRequestException() {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }

    public BadRequestException(ExceptionSetting.StackTraceDecision logStackTrace) {
        super(logStackTrace);
    }

    public BadRequestException(Object[] parameters) {
        super(parameters);
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }
}
