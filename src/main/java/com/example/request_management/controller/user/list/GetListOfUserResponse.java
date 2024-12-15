package com.example.request_management.controller.user.list;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetListOfUserResponse {

    @Builder
    @Getter
    public static class Item {
        private String name;
    }

    private List<Item> items;
    private Integer pageSize;
    private Integer pageIndex;
    private Long totalElements;
}
