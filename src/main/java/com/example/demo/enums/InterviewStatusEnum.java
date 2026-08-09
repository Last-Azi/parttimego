package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterviewStatusEnum {

    PENDING(0, "待确认"),
    ACCEPTED(1, "已接受"),
    REJECTED(2, "已拒绝"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final int code;
    private final String desc;

    public static InterviewStatusEnum fromCode(int code) {
        for (InterviewStatusEnum e : values()) {
            if (e.code == code) return e;
        }
        return PENDING;
    }
}
