package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobStatusEnum {

    PENDING(0, "待审核"),
    APPROVED(1, "已发布"),
    OFFLINE(2, "已下架"),
    REJECTED(3, "审核拒绝");

    private final int code;
    private final String desc;
}
