package com.agile.taskboard.mapper;

import com.agile.taskboard.entity.Worklog;
import com.agile.taskboard.vo.MemberStatVO;
import com.agile.taskboard.vo.WorklogVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WorklogMapper extends BaseMapper<Worklog> {

    List<WorklogVO> selectWorklogPage(@Param("projectId") Long projectId,
                                       @Param("userId") Long userId,
                                       @Param("status") Integer status);

    List<MemberStatVO> selectMemberStats(@Param("projectId") Long projectId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    List<java.util.Map<String, Object>> selectDailyTrend(@Param("projectId") Long projectId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);
}
