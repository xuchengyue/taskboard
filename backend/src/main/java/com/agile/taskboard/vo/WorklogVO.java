package com.agile.taskboard.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WorklogVO {

    private Long id;
    private Long taskId;
    private String taskTitle;
    private Long projectId;
    private String projectName;
    private Long userId;
    private String userName;
    private LocalDate workDate;
    private BigDecimal hours;
    private String description;
    private Integer status;
    private Long auditorId;
    private String auditorName;
    private String auditRemark;
    private LocalDateTime auditTime;
    private LocalDateTime createTime;
}
