package com.ksyun.start.camp.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksyun.start.camp.ApiResponse;
import com.ksyun.start.camp.entity.ResDataInfo;
import com.ksyun.start.camp.entity.ServiceInfo;
import com.ksyun.start.camp.utils.JacksonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 代表远端时间服务接口实现
 */
@Component
public class TimeServiceImpl implements TimeService {

    private static final JacksonMapper jacksonMapper = new JacksonMapper(JsonInclude.Include.NON_NULL);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String getDateTime(String style) {
        // Step 1: 获取远程服务列表
        RestTemplate restTemplate = new RestTemplate();
        String services = restTemplate.getForObject("http://localhost:8180/api/discovery?name=time-service", String.class);
        List<ServiceInfo> service = jacksonMapper.fromJson(services, new TypeReference<List<ServiceInfo>>() {});
        if (service == null || service.isEmpty()) {
            throw new RuntimeException("服务未注册！");
        }

        // Step 2: 调用远程服务
        ServiceInfo selectedService = service.get(0); // 这里可以根据负载均衡策略选择合适的服务实例
        String uri = String.format("http://%s:%d/api/getDateTime", selectedService.getIpAddress(), selectedService.getPort());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri)
                .queryParam("style", style);

        ApiResponse response = restTemplate.getForObject(builder.toUriString(), ApiResponse.class);
        if (response == null || response.getData() == null) {
            throw new RuntimeException("获取时间失败！");
        }
        ResDataInfo timeDate = objectMapper.convertValue(response.getData(), ResDataInfo.class);
        String res = "";
        try {
            res = convertGMTToBeijingTime(timeDate.getResult());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    // 将GMT时间字符串转换为北京时间字符串
    // gmtTimeStr：GMT时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
    // 返回：转换后的北京时间字符串
    public String convertGMTToBeijingTime(String gmtTimeStr) throws ParseException {
        SimpleDateFormat gmtFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat beijingFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        beijingFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        Date gmtTime = gmtFormatter.parse(gmtTimeStr);
        String beijingTimeStr = beijingFormatter.format(gmtTime);

        return beijingTimeStr;
    }
}
