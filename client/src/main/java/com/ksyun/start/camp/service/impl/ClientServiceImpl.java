package com.ksyun.start.camp.service.impl;

import com.ksyun.start.camp.entity.ResInfo;
import com.ksyun.start.camp.service.ClientService;
import com.ksyun.start.camp.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 客户端服务实现
 */
@Component
public class ClientServiceImpl implements ClientService {

    @Autowired
    private TimeService timeService;

    @Value("${spring.application.id}")
    private String id;

    @Override
    public ResInfo getInfo() {
        ResInfo resInfo = new ResInfo();
        String time = timeService.getDateTime("full");
        if(time == null) {
            resInfo.setError("简单时间服务不可用!");
            return resInfo;
        }
        resInfo.setResult("Hello Kingsoft Clound Star Camp - " + id + " - " + time);
        return resInfo;
    }
}
