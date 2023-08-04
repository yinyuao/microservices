# 微服务架构作业

本项目实现了一个简单的微服务架构，包含以下组件：

1. 服务注册中心 (register)
2. 服务提供者 (service, 2节点)
3. 服务调用客户端 (client, 2节点)
4. 分布式日志收集服务端 (logging-service)

## 系统要求

- Java 8 (1.8)
- Spring Boot

## 项目结构

本项目包含四个模块：`register`、`service`、`client`和`logging-service`。

### register

服务注册中心模块。用于服务的注册和发现，以及处理心跳信息。

### service

服务提供者模块。运行时有两个实例，分别监听8280和8281端口。提供获取当前日期时间信息的API。

### client

服务调用客户端模块。运行时有两个实例，分别监听8300和8301端口。调用服务提供者的API，并提供客户端信息。

### logging-service

分布式日志收集服务端模块。用于接收客户端提交的日志记录。

## 启动指南

请按照以下顺序启动各个模块，确保服务注册中心在其他模块之前启动。

1. 启动服务注册中心

```bash
cd register
mvn spring-boot:run
```

2. 启动服务提供者

```bash
cd service
mvn spring-boot:run
mvn spring-boot:run --spring.profiles.active=replica
```

3. 启动服务调用客户端

```bash
cd client
mvn spring-boot:run
mvn spring-boot:run --spring.profiles.active=replica
```

4. 启动分布式日志收集服务端

```bash
cd logging-service
mvn spring-boot:run
```

## API文档

以下是各个模块的API文档：

### 服务注册中心 (register)

- POST /api/register: 服务注册
- POST /api/unregister: 服务注销
- POST /api/heartbeat: 心跳发送
- GET /api/discovery: 服务发现

### 服务提供者 (service)

- GET /api/getDateTime: 获取当前日期时间信息及节点信息

### 服务调用客户端 (client)

- GET /api/getInfo: 获取客户端信息

### 分布式日志收集服务端 (logging-service)

- POST /api/logging: 记录日志信息

- GET /api/list: 获取日志信息列表

## 接口输入输出测试

### 1. 服务注册中心 (register)

#### 1.1. 服务注册

- 请求方式: POST
- 接口路径: /api/register
- 请求示例:

```json
POST /api/register

{
    "serviceName": "time-service",
    "serviceId": "service-1",
    "ipAddress": "10.0.0.1",
    "port": 8280
}
```

- 响应示例:

```json
{
    "code": 200,
    "msg": "Service registered successfully.",
    "data": null
}
```

#### 1.2. 服务注销

- 请求方式: POST
- 接口路径: /api/unregister
- 请求示例:

```json
POST /api/unregister

{
    "serviceName": "time-service",
    "serviceId": "service-1",
    "ipAddress": "10.0.0.1",
    "port": 8280
}
```

- 响应示例:

```json
{
    "code": 200,
    "msg": "Service unregistered successfully.",
    "data": null
}
```

#### 1.3. 心跳发送

- 请求方式: POST
- 接口路径: /api/heartbeat
- 请求示例:

```json
POST /api/heartbeat

{
    "serviceId": "service-1",
    "ipAddress": "10.0.0.1",
    "port": 8280
}
```

- 响应示例:

```json
{
    "code": 200,
    "msg": "Heartbeat sent successfully.",
    "data": null
}
```

#### 1.4. 服务发现

- 请求方式: GET
- 接口路径: /api/discovery
- 请求示例: GET /api/discovery?name=time-service

- 响应示例:

```json
{
    "code": 200,
    "msg": "Service discovery successful.",
    "data": [
        {
            "serviceName": "time-service",
            "serviceId": "service-1",
            "ipAddress": "10.0.0.1",
            "port": 8280
        }
    ]
}
```

### 2. 服务提供者 (service)

#### 2.1. 获取当前日期时间信息及节点信息

- 请求方式: GET
- 接口路径: /api/getDateTime
- 请求示例: GET /api/getDateTime?style=full

- 响应示例:

```json
{
    "code": 200,
    "msg": "Get date and time information successful.",
    "data": {
        "result": "2023-07-25 12:34:56",
        "serviceId": "service-1"
    }
}
```

### 3. 服务调用客户端 (client)

#### 3.1. 获取客户端信息

- 请求方式: GET
- 接口路径: /api/getInfo
- 请求示例: GET /api/getInfo

- 响应示例:

```json
{
    "code": 200,
    "msg": "Get client information successful.",
    "data": {
        "result": "Hello Kingsoft Clound Star Camp - service-1 - 2023-07-25 12:34:56"
    }
}
```

### 4. 分布式日志收集服务端 (logging-service)

#### 4.1. 记录日志信息

- 请求方式: POST
- 接口路径: /api/logging
- 请求示例:

```json
POST /api/logging

{
    "serviceName": "client",
    "serviceId": "client-1",
    "datetime": "2023-07-25 12:34:56.235",
    "level": "info",
    "message": "Client status is OK."
}
```

- 响应示例:

```json
{
    "code": 200,
    "msg": "Logging successful.",
    "data": null
}
```

#### 4.2. 获取日志信息列表

- 请求方式: GET
- 接口路径: /api/list
- 请求示例: GET /api/list?service=service-1

- 响应示例:

```json
{
    "code": 200,
    "msg": "Get log list successful.",
    "data": [
        {
            "logId": 5,
            "serviceName": "service1",
            "serviceId": "service-1",
            "datetime": "2023-07-25 12:34:56.235",
            "level": "info",
            "message": "..."
        },
        // ... 其他日志条目
    ]
}
```

以上是各个模块的接口输入输出测试。

## 注意事项

- 请确保各个模块的端口号不冲突。

  
## 联系方式

如有任何问题或疑虑，请联系：

- 殷钰奥：[yinyuao@kingsoft.com]
- 项目仓库：[[yuao yin / wk3-exam · GitLab](http://120.92.88.48/yinyuao/wk3-exam)]