-- 初始化种子数据，全部密码都是 123456，方便演示测试
-- 用 INSERT IGNORE 保证 Spring Boot 每次重启重复执行也不会报主键/唯一键冲突

INSERT IGNORE INTO sys_user (id, username, password, real_name, email, role, status) VALUES
(1, 'admin',  '$2b$10$ImeeABsxPJkyYxRL7gLlD.9YdMchA4t6s7rW8NNoEAU6MRIi41/rG', '系统管理员', 'admin@demo.com',  'ADMIN',  1),
(2, 'pm001',  '$2b$10$ImeeABsxPJkyYxRL7gLlD.9YdMchA4t6s7rW8NNoEAU6MRIi41/rG', '张经理',   'pm001@demo.com',  'PM',     1),
(3, 'dev001', '$2b$10$ImeeABsxPJkyYxRL7gLlD.9YdMchA4t6s7rW8NNoEAU6MRIi41/rG', '李开发',   'dev001@demo.com', 'MEMBER', 1),
(4, 'dev002', '$2b$10$ImeeABsxPJkyYxRL7gLlD.9YdMchA4t6s7rW8NNoEAU6MRIi41/rG', '王前端',   'dev002@demo.com', 'MEMBER', 1),
(5, 'dev003', '$2b$10$ImeeABsxPJkyYxRL7gLlD.9YdMchA4t6s7rW8NNoEAU6MRIi41/rG', '赵测试',   'dev003@demo.com', 'MEMBER', 1);

INSERT IGNORE INTO project (id, name, description, owner_id, status, start_date, end_date) VALUES
(1, '敏捷任务面板与工时统计系统', '毕业设计课题项目本身，用来演示系统功能', 2, 'ACTIVE', '2026-08-05', '2026-11-15'),
(2, '内部OA系统改造', '示例项目二，用来验证多项目场景下的统计功能', 2, 'ACTIVE', '2026-07-01', '2026-10-30');

INSERT IGNORE INTO project_member (id, project_id, user_id) VALUES
(1, 1, 2), (2, 1, 3), (3, 1, 4), (4, 1, 5),
(5, 2, 2), (6, 2, 3), (7, 2, 4);

INSERT IGNORE INTO t_task (id, project_id, title, description, status, priority, assignee_id, creator_id, estimated_hours, actual_hours, due_date, sort_order) VALUES
(1, 1, '搭建Spring Boot工程骨架', '初始化Maven工程，引入MyBatis-Plus和Spring Security依赖', 'DONE', 'HIGH', 3, 2, 8, 6, '2026-08-08', 1),
(2, 1, '设计数据库表结构', '完成用户/项目/任务/工时四张核心表的建模', 'DONE', 'HIGH', 3, 2, 6, 7, '2026-08-10', 2),
(3, 1, '实现JWT登录鉴权', '基于Spring Security和JJWT实现无状态登录', 'DOING', 'HIGH', 3, 2, 10, 4, '2026-08-20', 1),
(4, 1, '看板拖拽交互开发', '前端用vue-draggable-plus实现任务卡片跨列拖拽', 'DOING', 'MEDIUM', 4, 2, 12, 5, '2026-08-25', 2),
(5, 1, '工时填报表单', '成员填报每日工时，含单日超24小时校验', 'REVIEW', 'MEDIUM', 4, 2, 8, 8, '2026-09-01', 1),
(6, 1, '工时审核流程', '项目经理审核通过/驳回工时并联动更新任务实际耗时', 'TODO', 'MEDIUM', 5, 2, 6, 0, '2026-09-10', 1),
(7, 1, '工时统计报表(ECharts)', '按成员/项目/日期维度聚合工时并可视化展示', 'TODO', 'LOW', 5, 2, 10, 0, '2026-09-15', 2),
(8, 2, '需求调研与访谈', '走访OA现有用户，整理改造需求清单', 'DONE', 'MEDIUM', 3, 2, 16, 15, '2026-07-15', 1),
(9, 2, '审批流引擎选型', '对比国产工作流引擎与自研方案', 'DOING', 'HIGH', 4, 2, 10, 3, '2026-08-05', 1);

INSERT IGNORE INTO t_task_log (id, task_id, operator_id, old_status, new_status, remark) VALUES
(1, 1, 3, NULL, 'TODO', '任务创建'),
(2, 1, 3, 'TODO', 'DOING', '开始搭建工程'),
(3, 1, 3, 'DOING', 'DONE', '工程骨架已跑通'),
(4, 3, 3, NULL, 'TODO', '任务创建'),
(5, 3, 3, 'TODO', 'DOING', '开始开发登录鉴权模块');

INSERT IGNORE INTO t_worklog (id, task_id, project_id, user_id, work_date, hours, description, status, auditor_id, audit_remark, audit_time) VALUES
(1, 1, 1, 3, '2026-08-06', 4.0, '搭建工程结构，配置pom依赖', 1, 2, '通过', '2026-08-07 09:00:00'),
(2, 1, 1, 3, '2026-08-07', 2.0, '联调MyBatis-Plus和MySQL连接', 1, 2, '通过', '2026-08-08 09:00:00'),
(3, 2, 1, 3, '2026-08-09', 7.0, '设计并建好四张核心表', 1, 2, '通过', '2026-08-11 09:00:00'),
(4, 3, 1, 3, '2026-08-18', 4.0, 'JwtTokenProvider基础功能开发', 0, NULL, NULL, NULL),
(5, 4, 1, 4, '2026-08-22', 5.0, '接入vue-draggable-plus跑通拖拽demo', 1, 2, '通过', '2026-08-23 09:00:00'),
(6, 5, 1, 4, '2026-08-30', 8.0, '完成工时填报表单及校验', 0, NULL, NULL, NULL),
(7, 8, 2, 3, '2026-07-10', 8.0, '访谈财务部门', 1, 2, '通过', '2026-07-11 09:00:00'),
(8, 8, 2, 3, '2026-07-11', 7.0, '访谈行政部门并整理文档', 1, 2, '通过', '2026-07-12 09:00:00'),
(9, 9, 2, 4, '2026-08-01', 3.0, '调研Flowable和Camunda', 2, 2, '描述太简单，请补充对比结论', '2026-08-02 09:00:00');
