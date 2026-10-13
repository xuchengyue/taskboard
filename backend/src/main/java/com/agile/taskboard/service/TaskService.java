package com.agile.taskboard.service;

import com.agile.taskboard.dto.TaskDTO;
import com.agile.taskboard.dto.TaskStatusDTO;
import com.agile.taskboard.vo.TaskVO;

import java.util.List;

public interface TaskService {

    List<TaskVO> getBoardTasks(Long projectId);

    TaskVO createTask(TaskDTO dto);

    TaskVO updateTask(TaskDTO dto);

    void deleteTask(Long id);

    void updateStatus(TaskStatusDTO dto);
}
