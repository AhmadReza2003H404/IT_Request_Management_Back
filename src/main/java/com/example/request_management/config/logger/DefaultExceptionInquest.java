package com.example.request_management.config.logger;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultExceptionInquest implements ExceptionInquest {
    private Collection<DefinedException> definedExceptions;
    private static DefaultExceptionInquest defaultExceptionInquest = null;

    private DefaultExceptionInquest() {
        loadDefaultValues();
    }

    public static DefaultExceptionInquest getInstance() {
        if (defaultExceptionInquest == null) {
            defaultExceptionInquest = new DefaultExceptionInquest();
        }
        return defaultExceptionInquest;
    }

    @VisibleForTesting
    public Collection<DefinedException> getDefinedExceptions() {
        return definedExceptions;
    }

    @VisibleForTesting
    public void setDefinedExceptions(Collection<DefinedException> definedExceptions) {
        this.definedExceptions = definedExceptions;
    }

    protected void loadDefaultValues() {
        definedExceptions = new LinkedList<>();
        definedExceptions.add(
                DefinedException.builder()
                        .exceptionCategory(ExceptionCategory.BUSINESS_EXCEPTION)
                        .paths(List.of("digital.neobank.backofficemanager.config.exception"))
                        .logGrade(LogGrade.IGNORE)
                        .build()
        );
        definedExceptions.add(
                DefinedException.builder()
                        .exceptionCategory(ExceptionCategory.FEIGN_EXCEPTION)
                        .paths(List.of("digital.neobank.backofficemanager.config.feign"))
                        .logGrade(LogGrade.FULL_STACK_TRACE)
                        .build()
        );
        definedExceptions.add(
                DefinedException.builder()
                        .exceptionCategory(ExceptionCategory.SPRING_EXCEPTION)
                        .paths(List.of("org.spring", "org.springframework", "org.apache"))
                        .logGrade(LogGrade.ADDRESS)
                        .build()
        );
        definedExceptions.add(
                DefinedException.builder()
                        .exceptionCategory(ExceptionCategory.UNEXPECTED_EXCEPTION)
                        .paths(List.of(""))
                        .logGrade(LogGrade.FULL_STACK_TRACE)
                        .build()
        );
    }


    @Override
    public String getLog(Throwable throwable) {
        return findBestCase(throwable).map(it -> appropriateMessage(it, throwable)).orElse(null);
    }

    @Override
    public LogGrade getLogGrade(Throwable throwable) {
        return findBestCase(throwable).map(Triple::getRight).orElse(LogGrade.FULL_STACK_TRACE);
    }

    @Override
    public ExceptionCategory getCategory(Throwable throwable) {
        return findBestCase(throwable).map(Triple::getLeft).orElse(ExceptionCategory.UNEXPECTED_EXCEPTION);
    }

    private boolean isPackage(String path) {
        return path.equals(path.toLowerCase());
    }

    private String getAccessPath(Throwable throwable) {
        return throwable.getClass().getName();
    }

    private Optional<Triple<ExceptionCategory, String, LogGrade>> findBestCase(Throwable throwable) {
        return this.definedExceptions.stream().flatMap(it -> {
            var matchedCase = it.getPaths().stream().filter(inner -> {
                if (isPackage(inner)) {
                    return throwable.getClass().getPackage().getName().contains(inner);
                } else {
                    return getAccessPath(throwable).equals(inner);
                }
            }).map(inner ->
                    Triple.of(it.getExceptionCategory(), inner, it.getLogGrade())
            ).collect(Collectors.toList());
            return matchedCase.stream();
        }).sorted((o1, o2) -> Integer.compare(o2.getMiddle().split("\\.").length, o1.getMiddle().split("\\.").length))
                .collect(Collectors.toList()).stream().findFirst();
    }

    private String appropriateMessage(Triple<ExceptionCategory, String, LogGrade> l, Throwable throwable) {
        String lineTemplate = "Error occurred in class {%c} method {%m} line {%ln} message {%g}";
        switch (l.getRight()) {
            case FULL_STACK_TRACE:
                return Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining(","));
            case ERROR_MESSAGE:
                return throwable.getMessage();
            case ADDRESS:
                var topOfStack = throwable.getStackTrace()[0];
                var keys = new String[]{"%c", "%m", "%ln", "%g"};
                var values = new String[]{
                        topOfStack.getClassName(),
                        topOfStack.getMethodName(),
                        String.valueOf(topOfStack.getLineNumber()),
                        throwable.getMessage()
                };
                return StringUtils.replaceEach(lineTemplate, keys, values);
            case IGNORE:
                return null;
        }
        return null;
    }
}
