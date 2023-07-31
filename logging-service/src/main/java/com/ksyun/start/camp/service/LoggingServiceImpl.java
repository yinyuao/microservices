package com.ksyun.start.camp.service;

import com.ksyun.start.camp.cache.LogCache;
import com.ksyun.start.camp.entity.LogInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志服务的实现
 */
@Component
public class LoggingServiceImpl implements LoggingService {

    private LogCache logCache = new LogCache();

    @Override
    public void logging(LogInfo logInfo) {
        logCache.put(logInfo);
    }

    @Override
    public List<LogInfo> getList(String service) {
        return logCache.getList(service);
    }
}
