package com.agile.taskboard.controller;

import com.agile.taskboard.common.Result;
import com.agile.taskboard.dto.TaskDTO;
import com.agile.taskboard.dto.TaskStatusDTO;
import com.agile.taskboard.service.TaskService;
import com.agile.taskboard.vo.TaskVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/board")
    public Result<List<TaskVO>> board(@RequestParam Long projectId) {
        return Result.ok(taskService.getBoardTasks(projectId));
    }

    @PostMapping
    public Result<TaskVO> create(@Valid @RequestBody TaskDTO dto) {
        return Result.ok(taskService.createTask(dto));
    }

    @PutMapping
    public Result<TaskVO> update(@Valid @RequestBody TaskDTO dto) {
        return Result.ok(taskService.updateTask(dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return Result.ok();
    }

    @PutMapping("/status")
    public Result<Void> updateStatus(@Valid @RequestBody TaskStatusDTO dto) {
        taskService.updateStatus(dto);
        return Result.ok();
    }
}
