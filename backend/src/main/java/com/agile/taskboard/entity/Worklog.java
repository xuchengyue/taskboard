package com.agile.taskboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_worklog")
public class Worklog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private Long projectId;

    private Long userId;

    private LocalDate workDate;

    private BigDecimal hours;

    private String description;

    /** 0待审核 1通过 2驳回 */
    private Integer status;

    private Long auditorId;

    private String auditRemark;

    private LocalDateTime auditTime;

    private LocalDateTime createTime;
}
