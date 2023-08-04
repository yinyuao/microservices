package com.ksyun.start.camp.service.impl;

import com.ksyun.start.camp.entity.ResDataInfo;
import com.ksyun.start.camp.service.SimpleTimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 代表简单时间服务实现
 */
@Component
public class SimpleTimeServiceImpl implements SimpleTimeService {

    @Value("${spring.application.id}")
    private String id;

    @Override
    public Object getDateTime(String style) {
        String dateTime = formatDateTime(style);
        if(dateTime == null) {
            return null;
        }
        ResDataInfo res = createResDataInfo(dateTime);
        return res;
    }

    // 提取日期时间格式化逻辑为独立方法
    private String formatDateTime(String style) {
        switch (style) {
            case "full":
                return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(Instant.now().atZone(ZoneId.of("GMT")));
            case "date":
                return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(Instant.now().atZone(ZoneId.of("GMT")));
            case "time":
                return DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.now().atZone(ZoneId.of("GMT")));
            case "unix":
                return String.valueOf(Instant.now().toEpochMilli());
            default:
                return null;
        }
    }

    // 优化ResDataInfo对象的构造方式
    private ResDataInfo createResDataInfo(String dateTime) {
        ResDataInfo res = new ResDataInfo();
        res.setResult(dateTime);
        res.setServiceId(id);
        return res;
    }
}
