package com.example.request_management.config.exception;

public class NotAcceptableException extends BusinessException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.FIRST;
    }
}
