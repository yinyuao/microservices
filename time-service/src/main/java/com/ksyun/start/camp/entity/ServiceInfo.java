package com.ksyun.start.camp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ServiceInfo {

    private String serviceName;

    private String serviceId;

    private String ipAddress;

    private Integer port;

    @JsonIgnore
    private long timestamp;
}
