package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.entity.ServiceInfo;
import com.ksyun.start.camp.rest.ApiResponse;
import com.ksyun.start.camp.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("/register")
    public ApiResponse registry(@RequestBody ServiceInfo serviceInfo) {
        if(registerService.register(serviceInfo)) {
            return ApiResponse.success().msg("Service registered successfully.");
        } else {
            return ApiResponse.failure().msg("Service with the same id combination already registered.");
        }
    }

    @RequestMapping("/unregister")
    public ApiResponse unregister(@RequestBody ServiceInfo serviceInfo) {
        if(registerService.unregister(serviceInfo)) {
            return ApiResponse.success().msg("Service unregistered successfully.");
        } else {
            return ApiResponse.failure().msg("Service with the specified ID not found for unregistration.");
        }

    }

    @RequestMapping("/heartbeat")
    public ApiResponse heartbeat(@RequestBody ServiceInfo serviceInfo) {
        if(registerService.heartbeat(serviceInfo)) {
            return ApiResponse.success().msg("Heartbeat received and service status updated.");
        } else {
            return ApiResponse.failure().msg("Error occurred while processing heartbeat.");
        }
    }

    @RequestMapping("/discovery")
    public ApiResponse discovery(String name) {
        return ApiResponse.success().data(registerService.discovery(name)).msg("Service discovered successfully.");
    }
}
