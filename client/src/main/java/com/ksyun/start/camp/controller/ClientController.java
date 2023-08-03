package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.entity.ApiResponse;
import com.ksyun.start.camp.entity.ResInfo;
import com.ksyun.start.camp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认的客户端 API Controller
 */
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping("/getInfo")
    public ApiResponse getInfo() {
        ResInfo resInfo = clientService.getInfo();
        if(resInfo.getError() == null) {
            return ApiResponse.success(resInfo);
        } else {
            return ApiResponse.failure(resInfo);
        }
    }
}
