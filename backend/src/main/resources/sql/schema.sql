-- 敏捷任务面板与工时统计系统 建表脚本
-- 用 IF NOT EXISTS 是为了每次启动 Spring Boot 都能重复执行不报错

CREATE TABLE IF NOT EXISTS sys_user (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL COMMENT '登录账号',
    password      VARCHAR(100) NOT NULL COMMENT 'BCrypt加密密码',
    real_name     VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    email         VARCHAR(100)          DEFAULT NULL,
    avatar        VARCHAR(255)          DEFAULT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'MEMBER' COMMENT 'ADMIN/PM/MEMBER',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '1正常 0禁用',
    deleted       TINYINT      NOT NULL DEFAULT 0,
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS project (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(500)          DEFAULT NULL,
    owner_id      BIGINT       NOT NULL COMMENT '项目负责人(项目经理)ID',
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE/ARCHIVED',
    start_date    DATE                  DEFAULT NULL,
    end_date      DATE                  DEFAULT NULL,
    deleted       TINYINT      NOT NULL DEFAULT 0,
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

CREATE TABLE IF NOT EXISTS project_member (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id    BIGINT   NOT NULL,
    user_id       BIGINT   NOT NULL,
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_project_user (project_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目成员关联表';

CREATE TABLE IF NOT EXISTS t_task (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id        BIGINT       NOT NULL,
    title             VARCHAR(200) NOT NULL,
    description       TEXT                  DEFAULT NULL,
    status            VARCHAR(20)  NOT NULL DEFAULT 'TODO' COMMENT 'TODO/DOING/REVIEW/DONE',
    priority          VARCHAR(10)  NOT NULL DEFAULT 'MEDIUM' COMMENT 'LOW/MEDIUM/HIGH',
    assignee_id       BIGINT                DEFAULT NULL,
    creator_id        BIGINT       NOT NULL,
    estimated_hours   DECIMAL(6,1) NOT NULL DEFAULT 0,
    actual_hours       DECIMAL(6,1) NOT NULL DEFAULT 0,
    due_date          DATE                  DEFAULT NULL,
    sort_order        INT          NOT NULL DEFAULT 0 COMMENT '同一列内的排序',
    deleted           TINYINT      NOT NULL DEFAULT 0,
    create_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_project (project_id),
    KEY idx_assignee (assignee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

CREATE TABLE IF NOT EXISTS t_task_log (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id       BIGINT      NOT NULL,
    operator_id   BIGINT      NOT NULL,
    old_status    VARCHAR(20)          DEFAULT NULL,
    new_status    VARCHAR(20) NOT NULL,
    remark        VARCHAR(255)         DEFAULT NULL,
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_task (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务状态流转日志表';

CREATE TABLE IF NOT EXISTS t_worklog (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id        BIGINT        NOT NULL,
    project_id     BIGINT        NOT NULL COMMENT '冗余一份方便按项目统计,免得老是join',
    user_id        BIGINT        NOT NULL,
    work_date      DATE          NOT NULL,
    hours          DECIMAL(4,1)  NOT NULL,
    description    VARCHAR(500)           DEFAULT NULL,
    status         TINYINT       NOT NULL DEFAULT 0 COMMENT '0待审核 1通过 2驳回',
    auditor_id     BIGINT                 DEFAULT NULL,
    audit_remark   VARCHAR(255)           DEFAULT NULL,
    audit_time     DATETIME               DEFAULT NULL,
    create_time    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_task (task_id),
    KEY idx_user_date (user_id, work_date),
    KEY idx_project (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工时记录表';
