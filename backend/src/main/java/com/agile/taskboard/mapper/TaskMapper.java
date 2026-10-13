package com.agile.taskboard.mapper;

import com.agile.taskboard.entity.Task;
import com.agile.taskboard.vo.TaskVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    List<TaskVO> selectBoardTasks(@Param("projectId") Long projectId);

    int addActualHours(@Param("taskId") Long taskId, @Param("hours") BigDecimal hours);
}
