package com.ksyun.start.camp.service;

import com.ksyun.start.camp.cache.ServiceCache;
import com.ksyun.start.camp.entity.ServiceInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RegisterService {

    // 存储注册的服务信息的缓存
    private ServiceCache serviceCache = new ServiceCache();

    /**
     * 注册服务
     * @param serviceInfo 服务信息对象
     */
    public void register(ServiceInfo serviceInfo) {
        serviceCache.put(serviceInfo);
    }

    /**
     * 注销服务
     * @param serviceInfo 服务信息对象
     */
    public void unregister(ServiceInfo serviceInfo) {
        // 判断服务是否已注册，如果已注册则进行注销，否则打印提示信息
        if (serviceCache.containsService(serviceInfo.getServiceName(), serviceInfo)) {
            System.out.println("true");
            serviceCache.remove(serviceInfo.getServiceName(), serviceInfo.getServiceId());
        } else {
            System.out.println("false");
        }
    }

    /**
     * 处理服务心跳
     * @param serviceInfo 服务信息对象
     */
    public void heartbeat(ServiceInfo serviceInfo) {
        // 处理服务心跳，更新服务信息的时间戳
        serviceCache.processHeartbeat(serviceInfo.getServiceId());
    }

    /**
     * 服务发现
     * @param name 服务名称，如果为null，则返回所有可用的服务信息列表；否则返回指定服务名称的服务信息
     * @return 如果找到匹配的服务信息，则返回对应的服务信息；否则返回空list
     */
    public Object discovery(String name) {
        if (name == null) {
            // 如果name为null，则返回所有可用的服务信息列表
            return serviceCache.getAllServiceInfoList();
        }
        if (serviceCache.containsService(name)) {
            // 如果服务名称存在，则返回对应的服务信息
            return serviceCache.get(name);
        }
        // 如果服务名称不存在，则返回空字符串
        return new ArrayList<>();
    }
}
