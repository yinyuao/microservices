package com.ksyun.start.camp.service;

import com.ksyun.start.camp.cache.ServiceCache;
import com.ksyun.start.camp.entity.ServiceInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface RegisterService {


    boolean register(ServiceInfo serviceInfo);

    boolean unregister(ServiceInfo serviceInfo);

    boolean heartbeat(ServiceInfo serviceInfo);

    Object discovery(String name);
}
