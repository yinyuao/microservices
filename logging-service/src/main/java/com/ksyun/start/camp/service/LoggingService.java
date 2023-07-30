package com.ksyun.start.camp.service;

import com.ksyun.start.camp.entity.LogInfo;

import java.util.List;

/**
 * 日志服务实现接口
 */
public interface LoggingService {
    void logging(LogInfo logInfo);

    List<LogInfo> getList(String service);

}
