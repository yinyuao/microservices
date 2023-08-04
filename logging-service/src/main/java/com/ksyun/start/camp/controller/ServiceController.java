package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.entity.ApiResponse;
import com.ksyun.start.camp.entity.LogInfo;
import com.ksyun.start.camp.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现日志服务 API
 */
@RestController
@RequestMapping("/api")
public class ServiceController {

    @Autowired
    private LoggingService loggingService;

    @RequestMapping("/logging")
    public ApiResponse logging(@RequestBody LogInfo logInfo) {
        if (loggingService.logging(logInfo)) {
            return ApiResponse.success();
        } else {
            return ApiResponse.failure().msg("Element duplicated!");
        }
    }

    @RequestMapping("/list")
    public ApiResponse getList(String service) {
        return ApiResponse.success().data(loggingService.getList(service));
    }

}
