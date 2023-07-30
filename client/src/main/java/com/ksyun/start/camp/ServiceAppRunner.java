package com.ksyun.start.camp;

import com.ksyun.start.camp.entity.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * 服务启动运行逻辑
 */
@Slf4j
@Component
public class ServiceAppRunner implements ApplicationRunner {

    @Value("${server.port}")
    private int port;

    @Value("${spring.application.name}")
    private String name;

    @Value("${spring.application.id}")
    private String id;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 此处代码会在 Boot 应用启动时执行
        // 构建服务信息对象
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceName(name);
        serviceInfo.setIpAddress("127.0.0.1");
        serviceInfo.setPort(port);
        serviceInfo.setServiceId(id);
        // 开始编写你的逻辑，下面是提示
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://127.0.0.1:8180/api/register", serviceInfo, String.class);
        // 1. 向 registry 服务注册当前服务
        // 2. 定期发送心跳逻辑
        Thread heartbeatThread = new Thread(() -> {
            while (true) {
                try {
                    // 模拟发送心跳
                    restTemplate.postForObject("http://127.0.0.1:8180/api/heartbeat", serviceInfo, String.class);
                    log.info(name+"服务发送心跳包成功！");
                    // 休眠60秒
                    Thread.sleep(20 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        heartbeatThread.start();
    }

    @PreDestroy
    public void exit(){
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceName(name);
        serviceInfo.setIpAddress("127.0.0.1");
        serviceInfo.setPort(port);
        serviceInfo.setServiceId(id);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://127.0.0.1:8180/api/unregister", serviceInfo, String.class);
        log.info(name+"服务删除成功！");
    }
}
