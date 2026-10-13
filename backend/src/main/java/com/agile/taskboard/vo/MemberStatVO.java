package com.agile.taskboard.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberStatVO {

    private Long userId;
    private String memberName;
    private BigDecimal totalHours;
    private Integer taskCount;
}
