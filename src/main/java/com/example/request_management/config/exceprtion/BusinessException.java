package com.example.request_management.config.exceprtion;

import java.util.Arrays;

/**
 * {@code BusinessException} is the root of all business-related exceptions.
 * {@link #logStackTrace} is provided as protected, so every sub-class could directly manipulate the value. <br>
 * It is recommended to set {@link #logStackTrace} like {@link BadRequestException}.
 *
 * @implNote As each Exception has a particular inheritance hierarchy, the {@link #logStackTrace} could override in a
 * chain of hierarchies.
 */
public class BusinessException extends RuntimeException {
    protected ExceptionSetting.StackTraceDecision logStackTrace;

    private Object[] parameters;

    public Object[] getParameters() {
        return parameters;
    }

    public BusinessException(Object[] parameters) {
        this();
        this.parameters = parameters;
    }

    public BusinessException() {
        logStackTrace = ExceptionSetting.StackTraceDecision.FULL;
    }

    public BusinessException(ExceptionSetting.StackTraceDecision logStackTrace) {
        this.logStackTrace = logStackTrace;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        var stackTrace = super.getStackTrace();
        switch (logStackTrace) {
            case NONE:
                return new StackTraceElement[0];
            case FIRST:
                return Arrays.stream(stackTrace)
                        .findFirst()
                        .map(it -> new StackTraceElement[]{it})
                        .orElse(new StackTraceElement[0]);
            case PARTIAL:
                return Arrays.stream(stackTrace)
                        .toArray(StackTraceElement[]::new);
            default:
                return stackTrace;
        }
    }
}
