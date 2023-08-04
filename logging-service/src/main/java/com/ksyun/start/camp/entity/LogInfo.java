package com.ksyun.start.camp.entity;

import lombok.Data;

@Data
public class LogInfo {
    private Long logId;
    private String serviceName;
    private String serviceId;
    private String datetime;
    private String level;
    private String message;
}
