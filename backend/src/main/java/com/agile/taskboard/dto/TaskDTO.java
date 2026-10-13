package com.agile.taskboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TaskDTO {

    private Long id;

    @NotNull(message = "所属项目不能为空")
    private Long projectId;

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String description;

    private String priority = "MEDIUM";

    private Long assigneeId;

    private BigDecimal estimatedHours = BigDecimal.ZERO;

    private LocalDate dueDate;
}
