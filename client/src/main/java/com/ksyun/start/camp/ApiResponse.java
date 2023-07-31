package com.ksyun.start.camp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代表此 API 的返回对象
 */
@Data
@NoArgsConstructor
public class ApiResponse {

    /**
     * 代表此 API 的响应返回码
     * 200 表示成功，非 200 表示失败
     */
    private int code;

    private Object data;

    public ApiResponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }
    public ApiResponse code(int code) {
        this.code = code;
        return this;
    }

    public ApiResponse data(Object data) {
        this.data = data;
        return this;
    }

    public static ApiResponse success(Object data) {
        ApiResponse dto = new ApiResponse();
        dto.code(200);
        dto.data(data);
        return dto;
    }

    public static ApiResponse failure(Object data) {
        ApiResponse dto = new ApiResponse();
        dto.code(500);
        dto.data(data);
        return dto;
    }
}
