package com.example.request_management.config.exception;

public class TokenExpiredException extends SecurityException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }
}
