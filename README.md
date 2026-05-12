# 智慧农业管理系统

智慧农业管理系统是基于 Spring Boot + Maven 开发的多模块农业管理软件，后端进行多业务模块拆分，前端管理端使用 Vue 3 + TypeScript + Vite 进行开发。其中使用 HTTP 和 WebSocket 实现前后端数据交互与状态实时同步，保障农业管理业务的流畅运行

## 目前功能

### 系统管理
- 用户管理
- 角色与权限管理
- 字典管理
- 操作日志与登录日志

### 种植管理
- 任务管理
- 雇员与工时管理（人工/机械/农资）
- 地块管理
- 种质资源管理
- 农机与农资管理

### 仓储管理
- 库存查询与库存管理
- 入库与出库相关流程
- 仓库及库区管理

### 前端管理端
- 登录与权限校验
- 仪表盘及业务模块页面
- 与后端 API 联调（`/api` 代理）

## 技术选型

| 技术 | 说明 |
|---|---|
| Spring Boot 3.5.0 | 后端基础框架 |
| MyBatis-Plus 3.5.12 | ORM 框架 |
| MySQL | 关系型数据库 |
| Redis | 缓存与会话相关能力 |
| Maven | 多模块构建管理 |
| Vue 3 | 前端框架 |
| TypeScript | 前端开发语言 |
| Vite 5 | 前端构建工具 |
| Element Plus | 前端 UI 组件库 |
| Pinia | 前端状态管理 |
| Vue Router | 前端路由管理 |
| Axios | 前端 HTTP 请求 |
| UnoCSS | 原子化 CSS 工具 |

## 项目架构

```text
farm
├── farm_common        # 后端公共模块（通用工具、统一返回、异常处理等）
├── farm_system        # 后端系统管理模块（用户/权限/日志/字典）
├── farm_warehousing   # 后端仓储业务模块
├── farm_plant         # 后端种植业务模块
├── farm_main          # 后端启动聚合模块（统一启动入口）
├── farm-ui            # 前端管理端（Vue3 + Vite + TypeScript）
└── pom.xml            # Maven 父工程配置，管理多模块依赖版本
```

## 后端说明（重点）

### 启动入口

- 启动类：`com.itwork.farm_main.FarmMainApplication`
- 配置文件：`farm_main/src/main/resources/application.yml`
- 默认端口：`8080`

### 环境要求

- JDK 24
- Maven 3.8+
- MySQL（默认库名：`baizhanfarm`）
- Redis（默认：`localhost:6379`）

### 后端本地启动

```bash
mvn clean install
mvn -pl farm_main spring-boot:run
```

### 后端打包

```bash
mvn clean package -DskipTests
```

打包产物位于 `farm_main/target/`。

## 前端说明

前端目录为 `farm-ui`，采用 Vue 3 + TypeScript 技术栈。

### 环境要求

- Node.js 16+
- npm 8+（或 yarn / pnpm）

### 前端启动

```bash
cd farm-ui
npm install
npm run dev
```

### 前端打包

```bash
cd farm-ui
npm run build
```

## 联调方式

1. 启动 MySQL 与 Redis。
2. 启动后端（`farm_main`，端口 `8080`）。
3. 启动前端（`farm-ui`，端口 `3000`）。
4. 通过浏览器访问前端地址进行业务联调。

## 说明

- 本仓库已包含前后端完整代码。
- 提交代码前请避免提交敏感信息（如生产环境密码、密钥等）。
- 建议按模块提交，便于追踪变更与代码审阅。
