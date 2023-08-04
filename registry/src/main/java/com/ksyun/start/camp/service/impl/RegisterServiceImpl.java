package com.ksyun.start.camp.service.impl;

import com.ksyun.start.camp.cache.ServiceCache;
import com.ksyun.start.camp.entity.ServiceInfo;
import com.ksyun.start.camp.service.RegisterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    // 存储注册的服务信息的缓存
    private ServiceCache serviceCache = new ServiceCache();

    // 记录当前服务的索引用于轮询
    private static int currentIndex = 0;
    /**
     * 注册服务
     * @param serviceInfo 服务信息对象
     */
    public boolean register(ServiceInfo serviceInfo) {
        ServiceInfo service = serviceCache.findServiceInfoById(serviceInfo.getServiceId());
        if(service != null) {
            return false;
        }
        serviceCache.put(serviceInfo);
        return true;
    }

    /**
     * 注销服务
     * @param serviceInfo 服务信息对象
     */
    public boolean unregister(ServiceInfo serviceInfo) {
        // 判断服务是否已注册，如果已注册则进行注销，否则打印提示信息
        if (serviceCache.containsService(serviceInfo.getServiceName(), serviceInfo)) {
            serviceCache.remove(serviceInfo.getServiceName(), serviceInfo.getServiceId());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 处理服务心跳
     * @param serviceInfo 服务信息对象
     */
    public boolean heartbeat(ServiceInfo serviceInfo) {
        // 处理服务心跳，更新服务信息的时间戳
        ServiceInfo service = serviceCache.processHeartbeat(serviceInfo.getServiceId());
        if(service == null) {
            return false;
        }
        return true;
    }

    /**
     * 服务发现
     * @param name 服务名称，如果为null，则返回所有可用的服务信息列表在里面通过轮询选一个；否则返回指定服务名称的服务信息
     * @return 如果找到匹配的服务信息，则返回对应的服务信息；否则返回空list
     */
    public Object discovery(String name) {
        if (name == null) {
            // 如果name为null，则返回所有可用的服务信息列表
            return serviceCache.getAllServiceInfoList();
        }
        if (serviceCache.containsService(name)) {
            // 如果服务名称存在，则返回对应的服务信息
            List<ServiceInfo> list = serviceCache.get(name);
            List<ServiceInfo> res = new ArrayList<>();
            res.add(list.get(currentIndex++ % list.size()));
            return res;
        }
        // 如果服务名称不存在，则返回空字符串
        return new ArrayList<>();
    }
}
