package com.agile.taskboard.service;

import com.agile.taskboard.entity.SysUser;

import java.util.List;

public interface SysUserService {

    List<SysUser> listAll();

    SysUser getById(Long id);

    SysUser createUser(SysUser user);

    void updateStatus(Long id, Integer status);

    void resetPassword(Long id);
}
