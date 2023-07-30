package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.entity.ServiceInfo;
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
    public void registry(@RequestBody ServiceInfo serviceInfo) throws Exception {
        registerService.register(serviceInfo);
    }

    @RequestMapping("/unregister")
    public void unregister(@RequestBody ServiceInfo serviceInfo) {
        registerService.unregister(serviceInfo);
    }

    @RequestMapping("/heartbeat")
    public void heartbeat(@RequestBody ServiceInfo serviceInfo) {
        registerService.heartbeat(serviceInfo);
    }

    @RequestMapping("/discovery")
    public Object discovery(String name) {
        return registerService.discovery(name);
    }
}
