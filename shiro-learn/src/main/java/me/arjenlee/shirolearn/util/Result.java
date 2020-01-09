package me.arjenlee.shirolearn.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private String msg;
    private String code;
    private T data;

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(String code) {
        this.code = code;
    }

    public Result() {
    }
}
