package com.agile.taskboard.controller;

import com.agile.taskboard.common.Result;
import com.agile.taskboard.dto.ProjectDTO;
import com.agile.taskboard.entity.Project;
import com.agile.taskboard.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public Result<List<Project>> list() {
        return Result.ok(projectService.listMyProjects());
    }

    @PostMapping
    public Result<Project> create(@Valid @RequestBody ProjectDTO dto) {
        return Result.ok(projectService.createProject(dto));
    }

    @PutMapping
    public Result<Project> update(@Valid @RequestBody ProjectDTO dto) {
        return Result.ok(projectService.updateProject(dto));
    }

    @GetMapping("/{id}/members")
    public Result<List<Map<String, Object>>> members(@PathVariable("id") Long projectId) {
        return Result.ok(projectService.listMembers(projectId));
    }

    @PostMapping("/{id}/members/{userId}")
    public Result<Void> addMember(@PathVariable("id") Long projectId, @PathVariable Long userId) {
        projectService.addMember(projectId, userId);
        return Result.ok();
    }

    @DeleteMapping("/{id}/members/{userId}")
    public Result<Void> removeMember(@PathVariable("id") Long projectId, @PathVariable Long userId) {
        projectService.removeMember(projectId, userId);
        return Result.ok();
    }
}
