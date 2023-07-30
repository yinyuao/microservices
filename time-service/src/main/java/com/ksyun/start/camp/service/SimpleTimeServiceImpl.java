package com.ksyun.start.camp.service;

import com.ksyun.start.camp.entity.ServiceInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 代表简单时间服务实现
 */
@Component
public class SimpleTimeServiceImpl implements SimpleTimeService {

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
        RestTemplate restTemplate = new RestTemplate();
//        ServiceInfo serviceInfo = restTemplate.getForObject("http://127.0.0.1:8180/api/register?")
        Map<String, String> map = new HashMap<>();
        if (dateTime != null) {
            map.put("result", dateTime);
//            map.put("serviceId", );
        }
        return map;
    }
}
