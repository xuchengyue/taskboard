package com.agile.taskboard.service;

import com.agile.taskboard.dto.ProjectDTO;
import com.agile.taskboard.entity.Project;

import java.util.List;
import java.util.Map;

public interface ProjectService {

    List<Project> listMyProjects();

    Project createProject(ProjectDTO dto);

    Project updateProject(ProjectDTO dto);

    List<Map<String, Object>> listMembers(Long projectId);

    void addMember(Long projectId, Long userId);

    void removeMember(Long projectId, Long userId);
}
