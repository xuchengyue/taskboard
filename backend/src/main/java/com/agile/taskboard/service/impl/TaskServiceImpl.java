package com.agile.taskboard.service.impl;

import com.agile.taskboard.common.BusinessException;
import com.agile.taskboard.dto.TaskDTO;
import com.agile.taskboard.dto.TaskStatusDTO;
import com.agile.taskboard.entity.Task;
import com.agile.taskboard.entity.TaskLog;
import com.agile.taskboard.mapper.TaskLogMapper;
import com.agile.taskboard.mapper.TaskMapper;
import com.agile.taskboard.security.SecurityUtils;
import com.agile.taskboard.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Set<String> VALID_STATUS = Set.of("TODO", "DOING", "REVIEW", "DONE");

    private final TaskMapper taskMapper;
    private final TaskLogMapper taskLogMapper;

    @Override
    public List<com.agile.taskboard.vo.TaskVO> getBoardTasks(Long projectId) {
        return taskMapper.selectBoardTasks(projectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public com.agile.taskboard.vo.TaskVO createTask(TaskDTO dto) {
        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        task.setId(null);
        task.setStatus("TODO");
        task.setActualHours(BigDecimal.ZERO);
        task.setCreatorId(SecurityUtils.getCurrentUserId());
        task.setSortOrder(0);
        taskMapper.insert(task);

        TaskLog log = new TaskLog();
        log.setTaskId(task.getId());
        log.setOperatorId(SecurityUtils.getCurrentUserId());
        log.setOldStatus(null);
        log.setNewStatus("TODO");
        log.setRemark("任务创建");
        taskLogMapper.insert(log);

        com.agile.taskboard.vo.TaskVO vo = new com.agile.taskboard.vo.TaskVO();
        BeanUtils.copyProperties(task, vo);
        return vo;
    }

    @Override
    public com.agile.taskboard.vo.TaskVO updateTask(TaskDTO dto) {
        Task task = taskMapper.selectById(dto.getId());
        if (task == null) {
            throw new BusinessException("任务不存在");
        }
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setAssigneeId(dto.getAssigneeId());
        task.setEstimatedHours(dto.getEstimatedHours());
        task.setDueDate(dto.getDueDate());
        taskMapper.updateById(task);

        com.agile.taskboard.vo.TaskVO vo = new com.agile.taskboard.vo.TaskVO();
        BeanUtils.copyProperties(task, vo);
        return vo;
    }

    @Override
    public void deleteTask(Long id) {
        taskMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(TaskStatusDTO dto) {
        if (!VALID_STATUS.contains(dto.getStatus())) {
            throw new BusinessException("非法的任务状态: " + dto.getStatus());
        }

        Task task = taskMapper.selectById(dto.getTaskId());
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        String oldStatus = task.getStatus();
        if (oldStatus.equals(dto.getStatus())) {
            return;
        }

        task.setStatus(dto.getStatus());
        taskMapper.updateById(task);

        TaskLog log = new TaskLog();
        log.setTaskId(task.getId());
        log.setOperatorId(SecurityUtils.getCurrentUserId());
        log.setOldStatus(oldStatus);
        log.setNewStatus(dto.getStatus());
        taskLogMapper.insert(log);
    }
}
