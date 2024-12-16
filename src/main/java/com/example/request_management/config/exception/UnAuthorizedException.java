package com.example.request_management.config.exception;

public class UnAuthorizedException extends SecurityException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }
}
