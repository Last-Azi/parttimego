package com.example.demo.common;

import com.example.demo.enums.CodeEnum;

public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(CodeEnum codeEnum) {
        super(codeEnum.msg);
        this.code = codeEnum.code;
    }

    public int getCode() {
        return code;
    }
}
