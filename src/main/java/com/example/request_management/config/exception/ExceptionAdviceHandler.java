package com.example.request_management.config.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionAdviceHandler {
    final MessageSource messageSource;

    @Autowired
    public ExceptionAdviceHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorContent handle(BadRequestException e) {
        return translateException(e, null);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorContent handle(NotFoundException e) {
        return translateException(e, null);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorContent handle(TokenExpiredException e) {
        return translateException(e, null);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorContent handle(InvalidTokenException e) {
        return translateException(e, null);
    }

    @ExceptionHandler(NotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorContent handle(NotAcceptableException e) {
        return translateException(e, null);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorContent handle(UnAuthorizedException e) {
        return translateException(e, null);
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorContent handle(SecurityException e) {
        return translateException(e, null);
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorContent handle(InvalidFormatException e) {
        ErrorContent errorContent = translateException(e, null);

        errorContent.setFields(e.getPath().stream().map(
                        JsonMappingException.Reference::getFieldName
                ).collect(Collectors.toList())
        );

        return errorContent;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorContent handle(MethodArgumentNotValidException e) {
        ErrorContent errorContent = translateException(e, null);

        errorContent.setFields(e.getBindingResult().getFieldErrors().stream().map(
                        FieldError::getField
                ).collect(Collectors.toList())
        );
        return errorContent;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorContent handle(RuntimeException e) {
        logError(e);
        return translateException(new RuntimeException(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ErrorContent handle(AccessDeniedException e) {
        return translateException(e, null);
    }


    @ExceptionHandler(InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorContent handle(InternalServerError e) {
        return translateException(e, null);
    }

    private ErrorContent translateException(Exception e, Object[] parameters) {
        logError(e);
        return parseErrorMessage(messageSource.getMessage(e.getClass().getName(), parameters, Locale.getDefault()));
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
        if (e instanceof BusinessException)
            log.warn("Error Happened", e);

        else
            log.error("Error Happened", e);

        e.printStackTrace();
    }
}
