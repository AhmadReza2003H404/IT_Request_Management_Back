package com.example.request_management.controller.user.list;

import com.example.request_management.dto.UserDTO;
import com.example.request_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetListOfUserController {
    private final UserService userService;

    @GetMapping("/users")
    public GetListOfUserResponse createUser(@RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex) {
        Page<UserDTO> response = userService.getUsers(name, pageSize, pageIndex);

        return GetListOfUserResponse.builder()
                .items(userService.getUsers(name, pageSize, pageIndex).getContent().stream()
                        .map(userDTO -> GetListOfUserResponse.Item.builder()
                                .name(userDTO.getName())
                                .build())
                        .collect(Collectors.toList()))
                .pageSize(pageSize)
                .pageIndex(pageIndex)
                .totalElements(response.getTotalElements())
                .build();
    }
}
