package com.agile.taskboard.controller;

import com.agile.taskboard.common.Result;
import com.agile.taskboard.dto.AuditDTO;
import com.agile.taskboard.dto.WorklogDTO;
import com.agile.taskboard.service.WorklogService;
import com.agile.taskboard.vo.WorklogVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worklog")
@RequiredArgsConstructor
public class WorklogController {

    private final WorklogService worklogService;

    @PostMapping
    public Result<Void> submit(@Valid @RequestBody WorklogDTO dto) {
        worklogService.submit(dto);
        return Result.ok();
    }

    @GetMapping("/mine")
    public Result<List<WorklogVO>> mine() {
        return Result.ok(worklogService.myWorklogs());
    }

    @GetMapping("/pending")
    public Result<List<WorklogVO>> pending(@RequestParam(required = false) Long projectId) {
        return Result.ok(worklogService.pendingAudits(projectId));
    }

    @PutMapping("/audit")
    public Result<Void> audit(@Valid @RequestBody AuditDTO dto) {
        worklogService.audit(dto);
        return Result.ok();
    }
}
