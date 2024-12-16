package com.example.request_management.config.exception;

public class SecurityException extends BusinessException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.FULL;
    }
}
