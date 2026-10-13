package com.agile.taskboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskStatusDTO {

    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @NotBlank(message = "目标状态不能为空")
    private String status;
}
