package com.example.request_management.config.logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class DefinedException {
    private ExceptionInquest.ExceptionCategory exceptionCategory;
    private Collection<String> paths;
    private ExceptionInquest.LogGrade logGrade;
}
