package com.agile.taskboard.service;

import com.agile.taskboard.vo.MemberStatVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatService {

    List<MemberStatVO> memberStats(Long projectId, LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> dailyTrend(Long projectId, LocalDate startDate, LocalDate endDate);
}
