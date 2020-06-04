# Spring Cloud See

![Maven Central](https://img.shields.io/maven-central/v/com.upcwangying.cloud.see/spring-cloud-see-parent)

`Spring Cloud See`包含两个模块: 

- `See Auth`: 认证鉴权模块
- `See Swagger`: 对Swagger进行封装

## Spring Cloud See Auth

`See Auth`模块, 基于Spring自动装配机制开发.

### 配置

```yaml
see:
  auth:
    enabled: true         # 启用认证功能
    auth-filter-order: 3  # 默认为`3`. 此设置可改变zuul filter链中的filter的排序先后, 数值越小越靠前, 先执行. 注意: 此设置必须放在限流filter之后, 鉴权filter之前
    permission-filter-order: 4  # 默认为`4`. 注意: 此设置必须放在认证filter之后
    ignored-patterns:     # 忽略认证的URL
      POST:               # 请求方式 (POST)
      - /users/token
      GET:                # 请求方式 (GET)
      - /swagger-ui.html
      - /v2/api-docs
```

### TODO 

- [X] 认证功能
- [X] 鉴权功能

## Spring Cloud See Swagger

`See Swagger`模块, 基于Spring自动装配机制开发.

### 配置

```yml
see:
  swagger:
    enabled: true           # 是否启用
    exclude-services:       # 排除服务列表
      - eureka
      - zuul
      - gateway
    version: "2.0"          # 版本, 默认"2.0", 可忽略
    prefix: ${zuul.prefix}  # 与`zuul.prefix`配置一致, ${zuul.prefix}
    url: /v2/api-docs       # url, 默认"/v2/api-docs", 可忽略
```

## Deploy

```
mvn clean install deploy -P release -Dgpg.passphrase=生成秘钥时候你的密码
```
