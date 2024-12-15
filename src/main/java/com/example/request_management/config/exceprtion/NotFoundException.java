package com.example.request_management.config.exceprtion;

public class NotFoundException extends BusinessException {
    {
        super.logStackTrace = ExceptionSetting.StackTraceDecision.PARTIAL;
    }
}
