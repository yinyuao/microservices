package com.ksyun.start.camp.service;

import com.ksyun.start.camp.entity.ResDataInfo;
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

    // 枚举表示不同的时间格式
    private enum TimeStyle {
        FULL, DATE, TIME, UNIX
    }

    @Override
    public Object getDateTime(String style) {
        String dateTime = formatDateTime(style);
        ResDataInfo res = createResDataInfo(dateTime);
        return res;
    }

    // 提取日期时间格式化逻辑为独立方法
    private String formatDateTime(String style) {
        TimeStyle timeStyle = TimeStyle.valueOf(style.toUpperCase());
        switch (timeStyle) {
            case FULL:
                return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(Instant.now().atZone(ZoneId.of("GMT")));
            case DATE:
                return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(Instant.now().atZone(ZoneId.of("GMT")));
            case TIME:
                return DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.now().atZone(ZoneId.of("GMT")));
            case UNIX:
                return String.valueOf(Instant.now().toEpochMilli());
            default:
                throw new IllegalArgumentException("Invalid time style: " + style);
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
