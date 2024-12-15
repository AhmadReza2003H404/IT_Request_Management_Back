package com.example.request_management.config.exceprtion;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorContent {
    private int code;
    private String message;
    private List<String> fields;
}
