package com.agile.taskboard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditDTO {

    @NotNull(message = "工时记录ID不能为空")
    private Long worklogId;

    @NotNull(message = "审核结果不能为空")
    private Integer status;

    private String remark;
}
