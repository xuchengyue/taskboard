package com.agile.taskboard.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskVO {

    private Long id;
    private Long projectId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long assigneeId;
    private String assigneeName;
    private Long creatorId;
    private BigDecimal estimatedHours;
    private BigDecimal actualHours;
    private LocalDate dueDate;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
