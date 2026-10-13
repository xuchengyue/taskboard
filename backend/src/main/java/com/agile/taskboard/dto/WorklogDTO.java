package com.agile.taskboard.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WorklogDTO {

    @NotNull(message = "任务不能为空")
    private Long taskId;

    @NotNull(message = "工作日期不能为空")
    private LocalDate workDate;

    @NotNull(message = "工时不能为空")
    @DecimalMin(value = "0.5", message = "工时最少0.5小时")
    @DecimalMax(value = "24", message = "单次填报不能超过24小时")
    private BigDecimal hours;

    private String description;
}
