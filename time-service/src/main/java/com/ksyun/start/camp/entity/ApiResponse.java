package com.ksyun.start.camp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代表此 API 的返回对象
 */
@Data
@NoArgsConstructor
public class ApiResponse<T> {

    /**
     * 代表此 API 的响应返回码
     * 200 表示成功，非 200 表示失败
     */
    private int code;

    private T data;

    public ApiResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ApiResponse code(int code) {
        this.code = code;
        return this;
    }

    public ApiResponse data(T data) {
        this.data = data;
        return this;
    }

    public static ApiResponse success() {
        ApiResponse dto = new ApiResponse();
        dto.code(200);
        return dto;
    }

    public static ApiResponse failure() {
        ApiResponse dto = new ApiResponse();
        dto.code(500);
        return dto;
    }
}
