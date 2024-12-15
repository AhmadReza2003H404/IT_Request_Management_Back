package com.example.request_management.config.exceprtion;

import com.example.request_management.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Locale;

@Slf4j
@ControllerAdvice
public class ExceptionAdviceHandler {
    final MessageSource messageSource;

    public ExceptionAdviceHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // TODO: improvement needed
    @ExceptionHandler({UserNotFoundException.class, NotFoundException.class}) // Handle both exceptions
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorContent handle(NotFoundException e) {
        return translateException(e, null);
    }

    private ErrorContent translateException(Exception e, Object[] parameters) {
        logError(e);
        return parseErrorMessage(messageSource.getMessage(e.getClass().getName(), parameters, null));
    }

    private ErrorContent parseErrorMessage(String errorMessage) {
        String[] errorMessageItems = errorMessage.split("#");
        return ErrorContent.builder()
                .message(errorMessageItems[0])
                .code(Integer.parseInt(errorMessageItems[1]))
                .fields(Collections.emptyList())
                .build();
    }

    private void logError(Exception e) {
        log.error("Error Happened", e);
        e.printStackTrace();
    }
}
