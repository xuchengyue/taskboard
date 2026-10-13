package com.agile.taskboard.service.impl;

import com.agile.taskboard.common.BusinessException;
import com.agile.taskboard.dto.AuditDTO;
import com.agile.taskboard.dto.WorklogDTO;
import com.agile.taskboard.entity.Task;
import com.agile.taskboard.entity.Worklog;
import com.agile.taskboard.mapper.TaskMapper;
import com.agile.taskboard.mapper.WorklogMapper;
import com.agile.taskboard.security.SecurityUtils;
import com.agile.taskboard.service.WorklogService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorklogServiceImpl implements WorklogService {

    private final WorklogMapper worklogMapper;
    private final TaskMapper taskMapper;

    @Override
    public void submit(WorklogDTO dto) {
        Task task = taskMapper.selectById(dto.getTaskId());
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        Long userId = SecurityUtils.getCurrentUserId();

        // 校验单日累计工时不超过24小时，驳回的记录不计入
        List<Worklog> existing = worklogMapper.selectList(Wrappers.<Worklog>lambdaQuery()
                .eq(Worklog::getUserId, userId)
                .eq(Worklog::getWorkDate, dto.getWorkDate())
                .ne(Worklog::getStatus, 2));
        BigDecimal existedHours = existing.stream()
                .map(Worklog::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (existedHours.add(dto.getHours()).compareTo(new BigDecimal("24")) > 0) {
            throw new BusinessException("当日累计工时不能超过24小时，目前已填报 " + existedHours + " 小时");
        }

        Worklog worklog = new Worklog();
        worklog.setTaskId(dto.getTaskId());
        worklog.setProjectId(task.getProjectId());
        worklog.setUserId(userId);
        worklog.setWorkDate(dto.getWorkDate());
        worklog.setHours(dto.getHours());
        worklog.setDescription(dto.getDescription());
        worklog.setStatus(0);
        worklogMapper.insert(worklog);
    }

    @Override
    public List<com.agile.taskboard.vo.WorklogVO> myWorklogs() {
        return worklogMapper.selectWorklogPage(null, SecurityUtils.getCurrentUserId(), null);
    }

    @Override
    public List<com.agile.taskboard.vo.WorklogVO> pendingAudits(Long projectId) {
        return worklogMapper.selectWorklogPage(projectId, null, 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(AuditDTO dto) {
        if (dto.getStatus() != 1 && dto.getStatus() != 2) {
            throw new BusinessException("审核结果只能是通过(1)或驳回(2)");
        }

        Worklog worklog = worklogMapper.selectById(dto.getWorklogId());
        if (worklog == null) {
            throw new BusinessException("工时记录不存在");
        }
        if (worklog.getStatus() != 0) {
            throw new BusinessException("该工时记录已经被处理过了");
        }

        worklog.setStatus(dto.getStatus());
        worklog.setAuditorId(SecurityUtils.getCurrentUserId());
        worklog.setAuditRemark(dto.getRemark());
        worklog.setAuditTime(java.time.LocalDateTime.now());
        worklogMapper.updateById(worklog);

        if (dto.getStatus() == 1) {
            taskMapper.addActualHours(worklog.getTaskId(), worklog.getHours());
        }
    }
}
