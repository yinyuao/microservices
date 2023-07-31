package com.ksyun.start.camp;

import com.ksyun.start.camp.entity.LogInfo;
import com.ksyun.start.camp.entity.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private ScheduledExecutorService executorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 注册当前服务
        ServiceInfo serviceInfo = registerService();

        // 启动定时任务，定期发送心跳和日志
        executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(() -> sendHeartbeat(serviceInfo), 0, 56, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(() -> sendLog(), 0, 1, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void exit() {
        ServiceInfo serviceInfo = createServiceInfo();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://127.0.0.1:8180/api/unregister", serviceInfo, String.class);
        log.info(name + "服务删除成功！");

        // 停止定时任务
        executorService.shutdown();
    }

    /**
     * 注册当前服务到注册中心
     *
     * @return 注册后的服务信息对象
     */
    private ServiceInfo registerService() {
        ServiceInfo serviceInfo = createServiceInfo();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://127.0.0.1:8180/api/register", serviceInfo, String.class);
        return serviceInfo;
    }

    /**
     * 发送心跳包到注册中心，定期发送心跳保持服务活跃
     *
     * @param serviceInfo 当前服务的信息对象
     */
    private void sendHeartbeat(ServiceInfo serviceInfo) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("http://127.0.0.1:8180/api/heartbeat", serviceInfo, String.class);
            log.info(name + "服务发送心跳包成功！");
        } catch (Exception e) {
            log.error(name + "服务发送心跳包失败：" + e.getMessage());
        }
    }

    /**
     * 发送日志到日志中心，定期发送服务日志信息
     *
     */
    private void sendLog() {
        try {
            LogInfo logInfo = createLogInfo();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("http://127.0.0.1:8320/api/logging", logInfo, String.class);
            log.info(name + "发送日志成功！");
        } catch (Exception e) {
            log.error(name + "发送日志失败：" + e.getMessage());
        }
    }

    /**
     * 创建当前服务的信息对象
     *
     * @return 当前服务的信息对象
     */
    private ServiceInfo createServiceInfo() {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceName(name);
        serviceInfo.setIpAddress("127.0.0.1");
        serviceInfo.setPort(port);
        serviceInfo.setServiceId(id);
        return serviceInfo;
    }

    /**
     * 创建日志信息对象
     *
     * @return 日志信息对象
     */
    private LogInfo createLogInfo() {
        LogInfo logInfo = new LogInfo();
        logInfo.setServiceName(name);
        logInfo.setServiceId(id);
        logInfo.setLevel("level");
        logInfo.setMessage("status is OK.");
        logInfo.setDatetime(formatDateTime(Instant.now()));
        return logInfo;
    }

    /**
     * 格式化日期时间为指定格式
     *
     * @param instant 日期时间
     * @return 格式化后的日期时间字符串
     */
    private String formatDateTime(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.of("GMT"));
        return formatter.format(instant);
    }
}
