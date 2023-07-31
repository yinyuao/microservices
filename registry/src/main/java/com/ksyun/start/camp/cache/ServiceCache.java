package com.ksyun.start.camp.cache;

import com.ksyun.start.camp.entity.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceCache {

    private static final Map<String, List<ServiceInfo>> cache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static final int CACHE_EXPIRATION_TIME = 60; // 缓存失效时间，单位：秒


    // 初始化定时任务，定期清除过期的缓存
    static {
        scheduler.scheduleWithFixedDelay(ServiceCache::cleanupExpiredCache, 0, CACHE_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    /**
     * 将服务信息存入缓存，并为其添加时间戳
     * @param serviceInfo 服务信息对象
     */
    public static void put(ServiceInfo serviceInfo) {
        serviceInfo.setTimestamp(System.currentTimeMillis()); // 在put时设置时间戳
        cache.computeIfAbsent(serviceInfo.getServiceName(), k -> new ArrayList<>()).add(serviceInfo);
    }

    /**
     * 根据服务名称从缓存中获取服务信息列表
     * @param serviceName 服务名称
     * @return 对应的服务信息列表，如果不存在则返回空列表
     */
    public static List<ServiceInfo> get(String serviceName) {
        return cache.getOrDefault(serviceName, new ArrayList<>());
    }

    /**
     * 处理心跳请求并更新服务信息的Timestamp
     *
     * @param serviceId 服务ID
     */
    public static ServiceInfo processHeartbeat(String serviceId) {
        // 根据serviceId找到对应的服务信息
        ServiceInfo serviceInfo = findServiceInfoById(serviceId);

        if (serviceInfo != null) {
            // 更新服务信息的Timestamp为当前时间戳
            serviceInfo.setTimestamp(System.currentTimeMillis());
        }
        return serviceInfo;
    }


    /**
     * 根据服务ID查找服务信息
     * @param serviceId 服务ID
     * @return 对应的服务信息对象，如果不存在则返回null
     */
    public static ServiceInfo findServiceInfoById(String serviceId){
        // 遍历缓存中的所有服务信息
        for (List<ServiceInfo> serviceInfoList : cache.values()) {
            for (ServiceInfo serviceInfo : serviceInfoList) {
                // 如果找到与指定的serviceId匹配的服务信息，返回该服务信息对象
                if (serviceInfo.getServiceId().equals(serviceId)) {
                    return serviceInfo;
                }
            }
        }
        return null;
    }

    /**
     * 检查缓存中是否存在指定服务名称
     * @param serviceName 服务名称
     * @return 如果存在返回true，否则返回false
     */
    public static boolean containsService(String serviceName) {
        return cache.containsKey(serviceName);
    }

    /**
     * 检查缓存中是否存在指定服务名称
     * @param serviceName 服务名称
     * @param serviceInfo 服务信息对象
     * @return 如果存在返回true，否则返回false
     */
    public static boolean containsService(String serviceName, ServiceInfo serviceInfo) {
        // 使用get方法获取指定服务名称对应的服务信息列表
        List<ServiceInfo> serviceInfoList = cache.get(serviceName);

        if (serviceInfoList != null) {
            // 遍历服务信息列表，比较是否有匹配的ServiceInfo对象
            for (ServiceInfo existingServiceInfo : serviceInfoList) {
                if (existingServiceInfo.getServiceId().equals(serviceInfo.getServiceId()) &&
                        existingServiceInfo.getIpAddress().equals(serviceInfo.getIpAddress()) &&
                        existingServiceInfo.getPort().equals(serviceInfo.getPort())) {
                    return true; // 如果有匹配的对象，返回true
                }
            }
        }
        // 如果没有找到匹配的ServiceInfo对象，返回false
        return false;
    }


    /**
     * 从缓存中移除指定的服务信息
     * @param serviceName 服务名称
     * @param serviceId 服务ID
     */
    public static void remove(String serviceName, String serviceId) {
        cache.computeIfPresent(serviceName, (k, v) -> {
            v.removeIf(info -> info.getServiceId().equals(serviceId));
            return v;
        });
        if(cache.get(serviceName).size() == 0) {
            cache.remove(serviceName);
        }
    }

    /**
     * 清空缓存
     */
    public static void clear() {
        cache.clear();
    }

    /**
     * 获取所有服务信息的列表
     * @return 所有服务信息的列表
     */
    public static List<ServiceInfo> getAllServiceInfoList() {
        List<ServiceInfo> allServiceInfoList = new ArrayList<>();
        cache.values().forEach(allServiceInfoList::addAll);
        return allServiceInfoList;
    }

    /**
     * 获取缓存中的服务数量
     * @return 缓存中的服务数量
     */
    public static int size() {
        return cache.size();
    }

    /**
     * 判断缓存是否为空
     * @return 如果缓存为空返回true，否则返回false
     */
    public static boolean isEmpty() {
        return cache.isEmpty();
    }

    // 定时任务，清理过期缓存
    private static void cleanupExpiredCache() {
        long currentTime = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> {
            List<ServiceInfo> serviceInfos = entry.getValue();
            serviceInfos.removeIf(info -> currentTime - info.getTimestamp() > CACHE_EXPIRATION_TIME * 1000);
            return serviceInfos.isEmpty();
        });
    }
}
