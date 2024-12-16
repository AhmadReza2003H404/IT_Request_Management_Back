package com.example.request_management.config.exceprtion;

public class SecurityException extends BusinessException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.FULL;
    }
}
