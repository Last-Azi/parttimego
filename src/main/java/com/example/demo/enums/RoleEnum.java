package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    STUDENT("STUDENT", "学生"),
    EMPLOYER("EMPLOYER", "招聘方"),
    ADMIN("ADMIN", "管理员");

    private final String code;
    private final String desc;
}
