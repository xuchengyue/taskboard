# 敏捷任务面板与工时统计系统

配合毕业设计论文的完整前后端实现。后端 Spring Boot + MyBatis-Plus + MySQL，前端 Vue 3 + Element Plus。

## 目录结构

```
project/
├── backend/     Spring Boot 后端工程
└── frontend/    Vue 3 前端工程
```

## 环境准备

- JDK 17（已在本机 `D:\work\java\jdk17\jdk-17.0.20.1+1` 装好，用户级 JAVA_HOME/PATH 已配置，**新开的终端/IDE 窗口直接生效，无需重复设置**）
- Maven（本机已有 `D:\work\apache_maven`）
- Node.js / npm（本机已有）
- MySQL 8.0（本机已有 `D:\work\java\mysql-8.0.32-winx64`，服务名 `MySQL`，需要手动以管理员身份启动：`net start MySQL`）

## 启动步骤

### 1. 启动 MySQL

用管理员权限打开终端执行：

```powershell
net start MySQL
```

数据库账号密码见 `backend/src/main/resources/application.yml`（默认 root/123456），首次启动应用时会自动建库建表并插入演示数据（`schema.sql` + `data.sql`），不需要手动执行 SQL。

### 2. 启动后端

```powershell
cd project/backend
mvn spring-boot:run
```

默认监听 `http://localhost:8080/api`。

### 3. 启动前端

```powershell
cd project/frontend
npm run dev
```

默认监听 `http://localhost:5173`，已配置 `/api` 代理转发到后端 8080 端口。

## 演示账号

密码统一为 `123456`：

| 账号    | 角色     | 说明                 |
| ------- | -------- | -------------------- |
| admin   | 管理员   | 用户管理、全部项目    |
| pm001   | 项目经理 | 项目/任务创建、工时审核 |
| dev001  | 团队成员 | 看板操作、工时填报    |
| dev002  | 团队成员 | 同上                 |
| dev003  | 团队成员 | 同上                 |

## 功能模块

- 用户登录（JWT）与基于角色的权限区分（管理员/项目经理/团队成员）
- 项目管理（创建项目、维护项目成员）
- 敏捷看板：待办/进行中/待审核/已完成四栏拖拽流转
- 任务管理：创建、编辑、删除、分配、优先级、预估工时、截止日期
- 工时填报：按任务/日期填报，单日累计超过24小时会被拦截
- 工时审核：项目经理审核通过/驳回，通过后自动累加任务实际工时
- 工时统计：按成员/按日期维度的 ECharts 图表展示
