package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatusEnum {

    PENDING(0, "待处理"),
    VIEWED(1, "已查看"),
    INTERVIEW(2, "邀请面试"),
    ACCEPTED(3, "已录用"),
    REJECTED(4, "已拒绝");

    private final int code;
    private final String desc;

    public static ApplicationStatusEnum fromCode(int code) {
        for (ApplicationStatusEnum e : values()) {
            if (e.code == code) return e;
        }
        return PENDING;
    }
}
