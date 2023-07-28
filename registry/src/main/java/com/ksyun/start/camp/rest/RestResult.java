package com.ksyun.start.camp.rest;


import lombok.Data;

import java.io.Serializable;

/**
 * 响应DTO
 *
 * @author ksc
 **/
@Data
public class RestResult<T> implements Serializable {

    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常信息
     */
    private String msg;


    private String descr;


    /**
     * 返回结果
     */
    private T data;

    public T getData() {
        return data;
    }

    /**
     * 链式方法
     */
    public RestResult<T> code(int code) {
        this.code = code;
        return this;
    }


    public RestResult<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public RestResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public static RestResult success() {
        RestResult dto = new RestResult();
        dto.code(200).msg("OK");
        return dto;
    }

    public static RestResult failure() {
        RestResult dto = new RestResult();
        dto.code(500).msg("error");
        return dto;
    }

    public RestResult() {

    }
}
