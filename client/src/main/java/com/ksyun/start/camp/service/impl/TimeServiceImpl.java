package com.ksyun.start.camp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksyun.start.camp.entity.ApiResponse;
import com.ksyun.start.camp.entity.ResDataInfo;
import com.ksyun.start.camp.entity.ServiceInfo;
import com.ksyun.start.camp.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 代表远端时间服务接口实现
 */
@Component
public class TimeServiceImpl implements TimeService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String getDateTime(String style) {
        // Step 1: 获取远程服务列表
        List<ServiceInfo> service = getRemoteServices("time-service");
        if (service == null || service.isEmpty()) {
            return null;
        }

        // Step 2: 调用远程服务
        ServiceInfo selectedService = service.get(0); // 这里可以根据负载均衡策略选择合适的服务实例
        String uri = buildServiceUri(selectedService, style);
        RestTemplate restTemplate = new RestTemplate();
        ApiResponse response = restTemplate.getForObject(uri, ApiResponse.class);
        if (response == null || response.getData() == null) {
            return null;
        }

        ResDataInfo timeDate = objectMapper.convertValue(response.getData(), ResDataInfo.class);
        String res = "";
        try {
            res = convertGMTToBeijingTime(timeDate.getResult());
        } catch (ParseException e) {
            return null;
        }
        return res;
    }

    // 获取远程服务列表
    private List<ServiceInfo> getRemoteServices(String serviceName) {
        RestTemplate restTemplate = new RestTemplate();
        ApiResponse result = restTemplate.getForObject("http://localhost:8180/api/discovery?name=" + serviceName, ApiResponse.class);
        return objectMapper.convertValue(result.getData(), new TypeReference<List<ServiceInfo>>() {});
    }

    // 构建远程服务调用的URI
    private String buildServiceUri(ServiceInfo serviceInfo, String style) {
        String uri = String.format("http://%s:%d/api/getDateTime", serviceInfo.getIpAddress(), serviceInfo.getPort());
        return UriComponentsBuilder.fromUriString(uri)
                .queryParam("style", style)
                .toUriString();
    }

    // 将GMT时间字符串转换为北京时间字符串
    // gmtTimeStr：GMT时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
    // 返回：转换后的北京时间字符串
    public String convertGMTToBeijingTime(String gmtTimeStr) throws ParseException {
        SimpleDateFormat gmtFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat beijingFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        beijingFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        Date gmtTime;
        try {
            gmtTime = gmtFormatter.parse(gmtTimeStr);
        } catch (ParseException e) {
            throw new ParseException("Unable to parse GMT time", e.getErrorOffset());
        }

        String beijingTimeStr;
        try {
            beijingTimeStr = beijingFormatter.format(gmtTime);
        } catch (Exception e) {
            throw new ParseException("Unable to format Beijing time", 0);
        }

        return beijingTimeStr;
    }
}
