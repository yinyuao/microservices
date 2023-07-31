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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ServiceAppRunner implements ApplicationRunner {

    @Value("${server.port}")
    private int port;

    @Value("${spring.application.name}")
    private String name;

    @Value("${spring.application.id:}")
    private String id;

    private final RestTemplate restTemplate = new RestTemplate();

    // 初始化定时任务，定期发送心跳
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 注册服务
        registerService();
        // 定期发送心跳
        startHeartbeat();
    }

    @PreDestroy
    public void exit() {
        // 注销服务
        unregisterService();
        // 关闭定时任务
        scheduler.shutdown();
    }

    // 注册当前服务
    private void registerService() {
        ServiceInfo serviceInfo = buildServiceInfo();
        restTemplate.postForObject("http://127.0.0.1:8180/api/register", serviceInfo, String.class);
        log.info("{}服务注册成功！", name);
    }

    // 注销当前服务
    private void unregisterService() {
        ServiceInfo serviceInfo = buildServiceInfo();
        restTemplate.postForObject("http://127.0.0.1:8180/api/unregister", serviceInfo, String.class);
        log.info("{}服务注销成功！", name);
    }

    // 构建服务信息对象
    private ServiceInfo buildServiceInfo() {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceName(name);
        serviceInfo.setIpAddress("127.0.0.1");
        serviceInfo.setPort(port);
        serviceInfo.setServiceId(id);
        return serviceInfo;
    }

    // 开始定时发送心跳
    private void startHeartbeat() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                ServiceInfo serviceInfo = buildServiceInfo();
                restTemplate.postForObject("http://127.0.0.1:8180/api/heartbeat", serviceInfo, String.class);
                log.info("{}服务发送心跳包成功！", name);
            } catch (Exception e) {
                log.error("{}服务发送心跳包失败！", name, e);
            }
        }, 0, 56, TimeUnit.SECONDS); // 56秒为心跳发送间隔
    }
}
