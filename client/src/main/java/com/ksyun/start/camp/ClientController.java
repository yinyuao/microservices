package com.ksyun.start.camp;

import com.ksyun.start.camp.service.ClientService;
import com.ksyun.start.camp.service.TimeService;
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
        return ApiResponse.success(clientService.getInfo());
    }
}
