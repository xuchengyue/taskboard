package com.agile.taskboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_task_log")
public class TaskLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private Long operatorId;

    private String oldStatus;

    private String newStatus;

    private String remark;

    private LocalDateTime createTime;
}
