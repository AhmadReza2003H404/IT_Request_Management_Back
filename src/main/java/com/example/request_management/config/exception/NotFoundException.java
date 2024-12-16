package com.example.request_management.config.exception;

public class NotFoundException extends BusinessException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }
}
