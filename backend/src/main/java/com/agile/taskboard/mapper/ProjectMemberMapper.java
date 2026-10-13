package com.agile.taskboard.mapper;

import com.agile.taskboard.entity.ProjectMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {

    @Select("SELECT u.id, u.username, u.real_name, u.real_name AS realName, u.email, u.role " +
            "FROM project_member pm LEFT JOIN sys_user u ON pm.user_id = u.id " +
            "WHERE pm.project_id = #{projectId} AND u.deleted = 0")
    List<java.util.Map<String, Object>> selectMembersByProject(@Param("projectId") Long projectId);
}
