# campus-second-hand

校园二手交易平台，一个适合 Java 初学者学习和写进实习简历的前后端分离项目。

## 项目简介

本项目模拟校园内二手商品交易场景，支持用户注册登录、商品发布、商品浏览、下单购买、商品收藏以及管理员审核与管理等功能。

项目整体采用前后端分离架构：

- 后端：`Spring Boot + MyBatis-Plus + MySQL + JWT`
- 前端：`Vue3 + Vite + Element Plus + Axios`

代码简单清晰，适合初学者阅读、运行、讲解和二次开发。

## 技术栈

### 后端

- Spring Boot 2.7.18
- MyBatis-Plus 3.5.5
- MySQL 8
- JWT（jjwt 0.11.5）
- Maven

### 前端

- Vue 3
- Vite 5
- Element Plus
- Axios
- Vue Router

## 核心功能

### 1. 用户模块

- 用户注册
- 用户登录
- JWT 登录认证
- 普通用户 / 管理员角色区分

### 2. 商品模块

- 发布二手商品
- 商品列表查询
- 分类筛选
- 关键词搜索
- 商品详情查看
- 商品状态管理：待审核、已上架、已下架
- 商品图片上传与展示

### 3. 订单模块

- 用户下单购买商品
- 查看我的订单
- 查看我卖出的订单
- 订单状态管理：待交易、已完成、已取消

### 4. 收藏模块

- 收藏商品
- 取消收藏
- 查看我的收藏

### 5. 后台管理模块

- 管理员查看所有用户
- 管理员审核商品
- 管理员下架商品
- 管理员查看所有订单
- 管理员查看简单数据统计

## 项目结构

```text
campus-second-hand
├─ campus-second-hand-backend    # Spring Boot 后端
└─ campus-second-hand-frontend   # Vue3 前端
```

### 后端目录说明

```text
campus-second-hand-backend
├─ sql                         # 数据库建表 SQL
├─ src/main/java/com/campus
│  ├─ common                   # 公共返回结果、全局异常处理
│  ├─ config                   # MVC 配置
│  ├─ controller               # 控制层
│  ├─ dto                      # 请求参数对象
│  ├─ entity                   # 实体类
│  ├─ interceptor              # JWT 拦截器
│  ├─ mapper                   # Mapper 接口
│  ├─ service                  # 业务层
│  ├─ utils                    # 工具类
│  └─ vo                       # 返回对象
└─ src/main/resources
   └─ application.yml          # 后端配置文件
```

### 前端目录说明

```text
campus-second-hand-frontend
├─ public/images              # 商品静态图片目录
├─ src
│  ├─ api                     # 接口请求封装
│  ├─ components              # 公共组件
│  ├─ router                  # 路由配置
│  ├─ utils                   # 工具方法
│  └─ views                   # 页面组件
└─ package.json
```

## 数据库设计

项目主要包含以下数据表：

- `user` 用户表
- `category` 商品分类表
- `product` 商品表
- `orders` 订单表
- `favorite` 收藏表

完整建表与测试数据脚本见：

- [campus_second_hand.sql](./campus-second-hand-backend/sql/campus_second_hand.sql)

## 运行环境

建议环境：

- JDK 17 或以上
- Maven 3.8+
- MySQL 8.x
- Node.js 18+ / npm 9+

说明：

- 项目 `pom.xml` 中配置的 Java 版本为 `17`
- 本项目在 JDK 21 环境下也已经完成过本地运行验证

## 启动步骤

### 第一步：导入数据库

1. 创建数据库 `campus_second_hand`
2. 执行 SQL 文件：

```text
campus-second-hand-backend/sql/campus_second_hand.sql
```

### 第二步：修改后端配置

打开文件：

- [application.yml](./campus-second-hand-backend/src/main/resources/application.yml)

根据你的本地 MySQL 环境修改：

- 数据库地址
- 用户名
- 密码

当前默认配置示例：

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_second_hand?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: 12345
```

### 第三步：启动后端

进入后端目录：

```bash
cd campus-second-hand-backend
```

启动命令：

```bash
mvn spring-boot:run
```

后端默认启动地址：

```text
http://localhost:8080
```

### 第四步：启动前端

进入前端目录：

```bash
cd campus-second-hand-frontend
```

安装依赖：

```bash
npm install
```

启动项目：

```bash
npm run dev
```

前端默认访问地址一般为：

```text
http://127.0.0.1:5173
```

## 测试账号

SQL 中已内置测试账号：

### 管理员

- 用户名：`admin`
- 密码：`123456`

### 普通用户

- 用户名：`user1`
- 密码：`123456`

- 用户名：`user2`
- 密码：`123456`

## 商品图片说明

本项目支持两种商品图片方式：

### 1. 手动填写图片地址

将图片放到：

```text
campus-second-hand-frontend/public/images
```

发布商品时填写：

```text
/images/xxx.jpg
```

例如：

```text
/images/keyboard.jpg
```

### 2. 页面直接上传图片

项目已经实现商品图片上传功能：

- 前端可在发布商品页面直接选择图片并上传
- 上传成功后会自动回填图片地址
- 图片会保存到前端 `public/images` 目录中

说明：

- 默认最大上传大小为 `5MB`
- 支持 `jpg`、`jpeg`、`png`、`gif`、`webp`

## 接口说明

项目接口采用 RESTful 风格，主要接口示例如下：

### 用户接口

- `POST /user/register` 用户注册
- `POST /user/login` 用户登录

### 商品接口

- `POST /product/add` 发布商品
- `GET /product/list` 商品列表
- `GET /product/detail/{id}` 商品详情
- `GET /product/my` 我的商品

### 分类接口

- `GET /category/list` 分类列表

### 订单接口

- `POST /order/create/{productId}` 创建订单
- `GET /order/my/buy` 查看我买到的订单
- `GET /order/my/sell` 查看我卖出的订单

### 收藏接口

- `POST /favorite/add/{productId}` 添加收藏
- `DELETE /favorite/cancel/{productId}` 取消收藏
- `GET /favorite/my` 我的收藏

### 管理员接口

- `GET /admin/user/list` 用户列表
- `GET /admin/product/list` 商品审核列表
- `PUT /admin/product/status` 审核/下架商品
- `GET /admin/order/list` 所有订单
- `GET /admin/stats` 简单统计

## 项目亮点

- 采用前后端分离架构，覆盖完整的小型业务闭环
- 使用 JWT + 拦截器实现登录认证
- 使用角色字段区分普通用户和管理员
- 商品支持分类、搜索、审核、上下架
- 支持商品图片上传与详情页图片展示
- 使用统一返回结果 `Result`，便于前后端联调
- 代码风格偏教学型，适合初学者学习与汇报

## 适合学习的点

如果你是 Java 初学者，这个项目适合你练习：

- Spring Boot 项目基本结构
- MyBatis-Plus 的常用 CRUD
- 前后端分离项目联调
- JWT 登录认证流程
- 商品、订单、收藏等常见业务模块设计
- Vue3 页面开发与 Axios 请求封装

## 后续可扩展方向

- 商品分页
- 密码加密存储
- 参数校验
- 全局日志记录
- 接口文档（Swagger / Knife4j）
- 评论 / 留言功能
- 聊天功能
- 文件上传到云存储

## 说明

本项目定位为练习型项目，优先保证：

- 功能完整
- 结构清晰
- 方便运行
- 方便讲解

如果你觉得这个项目对你有帮助，欢迎自己继续完善。
