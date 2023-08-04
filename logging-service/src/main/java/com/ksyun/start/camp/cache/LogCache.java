package com.ksyun.start.camp.cache;

import com.ksyun.start.camp.entity.LogInfo;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogCache {

    private static final List<LogInfo> cache = new CopyOnWriteArrayList<>();

    private Long count = 1L;

    public void put(LogInfo logInfo) {
        logInfo.setLogId(count++);
        cache.add(0, logInfo);
    }

    public List<LogInfo> getList(String serviceId) {
        if (serviceId == null) {
            return cache;
        }
        List<LogInfo> list = new CopyOnWriteArrayList<>();
        for (LogInfo item : cache) {
            if (item.getServiceId().equals(serviceId)) {
                list.add(item);
            }
            if (list.size() >= 5) break;
        }
        return list;
    }

    public boolean hasDuplicates(LogInfo logInfo) {
        for (LogInfo cachedLog : cache) {
            LogInfo copyCachedLog = new LogInfo();
            copyCachedLog.setServiceName(cachedLog.getServiceName());
            copyCachedLog.setServiceId(cachedLog.getServiceId());
            copyCachedLog.setDatetime(cachedLog.getDatetime());
            copyCachedLog.setLevel(cachedLog.getLevel());
            copyCachedLog.setMessage(cachedLog.getMessage());

            if (Objects.equals(logInfo, copyCachedLog)) {
                return true;
            }
        }
        return false;
    }

}
