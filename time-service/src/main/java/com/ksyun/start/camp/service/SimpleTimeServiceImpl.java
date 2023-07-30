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

    @Override
    public Object getDateTime(String style) {
        String dateTime = null;
        switch (style) {
            case "full":
                dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(Instant.now().atZone(ZoneId.of("GMT")));
                break;
            case "date":
                dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(Instant.now().atZone(ZoneId.of("GMT")));
                break;
            case "time":
                dateTime = DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.now().atZone(ZoneId.of("GMT")));
                break;
            case "unix":
                dateTime = String.valueOf(Instant.now().toEpochMilli());
                break;
        }
        ResDataInfo res = new ResDataInfo();
        res.setResult(dateTime);
        res.setServiceId(id);
        return res;
    }
}
