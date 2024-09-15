package com.travel.global.security.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {

    ACCESS("access-token"),
    REFRESH("refresh-token"),
    MOCK("mock-token");

    private final String name;
}
