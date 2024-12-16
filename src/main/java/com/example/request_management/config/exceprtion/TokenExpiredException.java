package com.example.request_management.config.exceprtion;

public class TokenExpiredException extends SecurityException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }
}
