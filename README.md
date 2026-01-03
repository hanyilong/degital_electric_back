# 设备管理系统

## 项目概述

本项目是一个基于前后端分离架构的设备管理系统，主要实现物模型管理和设备管理功能。

## 技术栈

### 后端
- Spring Boot 2.7.15
- MyBatis 2.3.1
- MySQL 8.0
- Lombok

### 前端
- React 18.2.0
- React Router DOM 6.16.0
- Element React
- Axios

## 系统功能

### 物模型管理
- 物模型列表展示
- 添加物模型
- 编辑物模型
- 删除物模型
- 物模型包含：模型名称、描述、类型、属性、功能、事件

### 设备管理
- 设备列表展示
- 添加设备
- 编辑设备
- 删除设备
- 设备状态管理（在线/离线/未知）
- 设备与物模型关联

## 项目结构

```
device-management-source/
├── backend/                     # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/device/manage/
│   │   │   │   ├── entity/      # 实体类
│   │   │   │   ├── mapper/      # Mapper接口
│   │   │   │   ├── service/     # Service接口
│   │   │   │   ├── service/impl/ # Service实现
│   │   │   │   ├── controller/  # Controller
│   │   │   │   └── DeviceManageApplication.java # 启动类
│   │   │   └── resources/       # 配置文件
│   │   │       ├── application.properties # 应用配置
│   │   │       ├── ThingModelMapper.xml # 物模型Mapper配置
│   │   │       ├── DeviceMapper.xml # 设备Mapper配置
│   │   │       └── init.sql # 数据库初始化脚本
│   │   └── test/                # 测试代码
│   └── pom.xml                  # Maven配置
├── frontend/                    # 前端项目
│   ├── public/                  # 静态资源
│   ├── src/
│   │   ├── components/          # 组件
│   │   ├── pages/               # 页面
│   │   │   ├── ThingModelList.js # 物模型列表
│   │   │   └── DeviceList.js    # 设备列表
│   │   ├── utils/               # 工具类
│   │   │   └── api.js           # API调用
│   │   ├── App.js               # 应用组件
│   │   ├── App.css              # 应用样式
│   │   ├── index.js             # 入口文件
│   │   └── index.css            # 全局样式
│   └── package.json             # 项目配置
└── README.md                    # 项目说明
```

## 部署步骤

### 1. 数据库准备

1. 安装MySQL 8.0
2. 创建数据库并执行初始化脚本：

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE device_manage CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行初始化脚本
USE device_manage;
source /path/to/backend/src/main/resources/init.sql;
```

### 2. 后端部署

1. 修改配置文件 `backend/src/main/resources/application.properties`，配置数据库连接：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/device_manage?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
```

2. 构建并启动后端服务：

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080/api` 启动。

### 3. 前端部署

1. 安装依赖：

```bash
cd frontend
npm install
```

2. 启动前端开发服务器：

```bash
npm start
```

前端服务将在 `http://localhost:3000` 启动。

## API 接口

### 物模型接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/thing-model | 获取所有物模型 |
| GET | /api/thing-model/{id} | 根据ID获取物模型 |
| POST | /api/thing-model | 创建物模型 |
| PUT | /api/thing-model/{id} | 更新物模型 |
| DELETE | /api/thing-model/{id} | 删除物模型 |

### 设备接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/device | 获取所有设备 |
| GET | /api/device/{id} | 根据ID获取设备 |
| GET | /api/device/model/{modelId} | 根据物模型ID获取设备 |
| POST | /api/device | 创建设备 |
| PUT | /api/device/{id} | 更新设备 |
| DELETE | /api/device/{id} | 删除设备 |

## 使用说明

1. 访问前端页面 `http://localhost:3000`
2. 在左侧菜单栏选择"物模型管理"或"设备管理"
3. 物模型管理页面：
   - 点击"添加物模型"按钮创建新的物模型
   - 点击列表中的"编辑"按钮修改物模型
   - 点击列表中的"删除"按钮删除物模型
4. 设备管理页面：
   - 点击"添加设备"按钮创建设备
   - 点击列表中的"编辑"按钮修改设备
   - 点击列表中的"删除"按钮删除设备
   - 设备状态会以不同颜色的标签显示（在线：绿色，离线：红色，未知：黄色）

## 注意事项

1. 确保MySQL服务已启动并配置正确
2. 后端服务必须先启动，前端服务才能正常调用API
3. 物模型删除时，关联的设备也会被级联删除
4. 设备状态需要通过实际的设备连接来更新，本系统仅提供状态字段管理

## 扩展建议

1. 添加用户认证和授权功能
2. 实现设备数据采集和监控功能
3. 添加设备远程控制功能
4. 实现数据统计和可视化报表
5. 支持设备分组和批量操作
6. 添加日志管理和系统审计功能

后端启动命令：
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens=java.base/java.nio=org.apache.arrow.memory.core,ALL-UNNAMED"
```
