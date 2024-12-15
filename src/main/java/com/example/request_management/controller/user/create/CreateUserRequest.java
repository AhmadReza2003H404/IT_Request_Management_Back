package com.example.request_management.controller.user.create;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class CreateUserRequest {

    @NonNull
    private String name;
}
