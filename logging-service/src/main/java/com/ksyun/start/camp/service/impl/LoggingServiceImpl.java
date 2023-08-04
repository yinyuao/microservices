package com.ksyun.start.camp.service.impl;

import com.ksyun.start.camp.cache.LogCache;
import com.ksyun.start.camp.entity.LogInfo;
import com.ksyun.start.camp.service.LoggingService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志服务的实现
 */
@Component
public class LoggingServiceImpl implements LoggingService {

    private LogCache logCache = new LogCache();

    @Override
    public boolean logging(LogInfo logInfo) {
        // 如果存在重复就不执行
        if(logCache.hasDuplicates(logInfo)) {
            return false;
        }
        logCache.put(logInfo);
        return true;
    }

    @Override
    public List<LogInfo> getList(String service) {
        return logCache.getList(service);
    }
}
