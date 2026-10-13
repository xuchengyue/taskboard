package com.agile.taskboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_task")
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private String title;

    private String description;

    /** TODO / DOING / REVIEW / DONE */
    private String status;

    /** LOW / MEDIUM / HIGH */
    private String priority;

    private Long assigneeId;

    private Long creatorId;

    private BigDecimal estimatedHours;

    private BigDecimal actualHours;

    private LocalDate dueDate;

    private Integer sortOrder;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
