package com.example.request_management.config.logger;

interface ExceptionInquest {
    enum LogGrade {
        FULL_STACK_TRACE,
        ERROR_MESSAGE,
        ADDRESS,
        IGNORE
    }

    enum ExceptionCategory {
        BUSINESS_EXCEPTION,
        FEIGN_EXCEPTION,
        SPRING_EXCEPTION,
        UNEXPECTED_EXCEPTION
    }

    String getLog(Throwable throwable);

    LogGrade getLogGrade(Throwable throwable);

    ExceptionCategory getCategory(Throwable throwable);
}
