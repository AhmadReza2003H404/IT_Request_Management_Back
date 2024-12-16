package com.example.request_management.config.exception;

public class InvalidTokenException extends SecurityException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.FIRST;
    }
}
