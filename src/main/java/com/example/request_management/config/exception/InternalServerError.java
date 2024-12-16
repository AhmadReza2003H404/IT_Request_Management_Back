package com.example.request_management.config.exception;

public class InternalServerError extends BusinessException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.FULL;
    }
}
