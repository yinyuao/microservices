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

import java.util.List;
import java.util.Map;

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
            return null;
        }

        // Step 2: 调用远程服务
        ServiceInfo selectedService = service.get(0); // 这里可以根据负载均衡策略选择合适的服务实例
        String uri = String.format("http://%s:%d/api/getDateTime", selectedService.getIpAddress(), selectedService.getPort());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri)
                .queryParam("style", style);

        ApiResponse response = restTemplate.getForObject(builder.toUriString(), ApiResponse.class);
        if (response == null || response.getData() == null) {
            return null;
        }
        System.out.println(response.getData());
        ResDataInfo timeDate = objectMapper.convertValue(response.getData(), ResDataInfo.class);
        return timeDate.getResult();
    }
}
