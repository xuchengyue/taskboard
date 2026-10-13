package com.agile.taskboard.service.impl;

import com.agile.taskboard.mapper.WorklogMapper;
import com.agile.taskboard.service.StatService;
import com.agile.taskboard.vo.MemberStatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final WorklogMapper worklogMapper;

    @Override
    public List<MemberStatVO> memberStats(Long projectId, LocalDate startDate, LocalDate endDate) {
        return worklogMapper.selectMemberStats(projectId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> dailyTrend(Long projectId, LocalDate startDate, LocalDate endDate) {
        return worklogMapper.selectDailyTrend(projectId, startDate, endDate);
    }
}
