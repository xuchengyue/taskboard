package com.agile.taskboard.service.impl;

import com.agile.taskboard.common.BusinessException;
import com.agile.taskboard.dto.ProjectDTO;
import com.agile.taskboard.entity.Project;
import com.agile.taskboard.entity.ProjectMember;
import com.agile.taskboard.mapper.ProjectMapper;
import com.agile.taskboard.mapper.ProjectMemberMapper;
import com.agile.taskboard.security.SecurityUtils;
import com.agile.taskboard.service.ProjectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectMemberMapper projectMemberMapper;

    @Override
    public List<Project> listMyProjects() {
        if (SecurityUtils.isAdmin()) {
            return projectMapper.selectList(Wrappers.<Project>lambdaQuery().orderByDesc(Project::getCreateTime));
        }

        Long userId = SecurityUtils.getCurrentUserId();
        if (SecurityUtils.isPM()) {
            return projectMapper.selectList(Wrappers.<Project>lambdaQuery()
                    .eq(Project::getOwnerId, userId).orderByDesc(Project::getCreateTime));
        }

        // 普通成员只能看到自己参与的项目
        List<ProjectMember> memberships = projectMemberMapper.selectList(
                Wrappers.<ProjectMember>lambdaQuery().eq(ProjectMember::getUserId, userId));
        if (memberships.isEmpty()) {
            return List.of();
        }
        List<Long> projectIds = memberships.stream().map(ProjectMember::getProjectId).toList();
        return projectMapper.selectList(Wrappers.<Project>lambdaQuery().in(Project::getId, projectIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Project createProject(ProjectDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus("ACTIVE");
        project.setOwnerId(SecurityUtils.getCurrentUserId());
        projectMapper.insert(project);

        if (dto.getMemberIds() != null) {
            for (Long userId : dto.getMemberIds()) {
                addMember(project.getId(), userId);
            }
        }
        return project;
    }

    @Override
    public Project updateProject(ProjectDTO dto) {
        Project project = projectMapper.selectById(dto.getId());
        if (project == null) {
            throw new BusinessException("项目不存在");
        }
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        projectMapper.updateById(project);
        return project;
    }

    @Override
    public List<Map<String, Object>> listMembers(Long projectId) {
        return projectMemberMapper.selectMembersByProject(projectId);
    }

    @Override
    public void addMember(Long projectId, Long userId) {
        long exists = projectMemberMapper.selectCount(Wrappers.<ProjectMember>lambdaQuery()
                .eq(ProjectMember::getProjectId, projectId).eq(ProjectMember::getUserId, userId));
        if (exists > 0) {
            return;
        }
        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setUserId(userId);
        projectMemberMapper.insert(member);
    }

    @Override
    public void removeMember(Long projectId, Long userId) {
        projectMemberMapper.delete(Wrappers.<ProjectMember>lambdaQuery()
                .eq(ProjectMember::getProjectId, projectId).eq(ProjectMember::getUserId, userId));
    }
}
