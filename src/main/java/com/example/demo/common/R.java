package com.example.demo.common;

import com.example.demo.enums.CodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok() {
        return build(CodeEnum.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return build(CodeEnum.SUCCESS, data);
    }

    public static <T> R<T> ok(String msg, T data) {
        R<T> r = new R<>();
        r.setCode(CodeEnum.SUCCESS.getCode());
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static <T> R<T> fail(String msg) {
        R<T> r = new R<>();
        r.setCode(CodeEnum.FAIL.getCode());
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail(CodeEnum codeEnum) {
        return build(codeEnum, null);
    }

    private static <T> R<T> build(CodeEnum codeEnum, T data) {
        R<T> r = new R<>();
        r.setCode(codeEnum.getCode());
        r.setMsg(codeEnum.getMsg());
        r.setData(data);
        return r;
    }
}
