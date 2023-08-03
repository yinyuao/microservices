package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.entity.ApiResponse;
import com.ksyun.start.camp.service.SimpleTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ServiceController {
    
    @Autowired
    private SimpleTimeService simpleTimeService;

    // 在此实现简单时间服务的接口逻辑
    // 1. 调用 SimpleTimeService
    @RequestMapping("/getDateTime")
    public ApiResponse getDateTime(String style) {
        return ApiResponse.success(simpleTimeService.getDateTime(style));
    }

}
