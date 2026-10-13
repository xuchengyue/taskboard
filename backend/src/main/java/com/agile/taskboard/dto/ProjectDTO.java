package com.agile.taskboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "项目名称不能为空")
    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<Long> memberIds;
}
