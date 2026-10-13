package com.agile.taskboard.service;

import com.agile.taskboard.dto.AuditDTO;
import com.agile.taskboard.dto.WorklogDTO;
import com.agile.taskboard.vo.WorklogVO;

import java.util.List;

public interface WorklogService {

    void submit(WorklogDTO dto);

    List<WorklogVO> myWorklogs();

    List<WorklogVO> pendingAudits(Long projectId);

    void audit(AuditDTO dto);
}
