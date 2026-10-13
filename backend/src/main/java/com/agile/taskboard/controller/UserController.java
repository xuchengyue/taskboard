package com.agile.taskboard.controller;

import com.agile.taskboard.common.BusinessException;
import com.agile.taskboard.common.Result;
import com.agile.taskboard.entity.SysUser;
import com.agile.taskboard.security.SecurityUtils;
import com.agile.taskboard.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;

    @GetMapping("/me")
    public Result<SysUser> me() {
        SysUser user = sysUserService.getById(SecurityUtils.getCurrentUserId());
        user.setPassword(null);
        return Result.ok(user);
    }

    @GetMapping("/list")
    public Result<List<SysUser>> list() {
        List<SysUser> users = sysUserService.listAll();
        users.forEach(u -> u.setPassword(null));
        return Result.ok(users);
    }

    @PostMapping
    public Result<SysUser> create(@RequestBody SysUser user) {
        checkAdmin();
        return Result.ok(sysUserService.createUser(user));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        checkAdmin();
        sysUserService.updateStatus(id, body.get("status"));
        return Result.ok();
    }

    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        checkAdmin();
        sysUserService.resetPassword(id);
        return Result.ok();
    }

    private void checkAdmin() {
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException(403, "只有管理员才能执行此操作");
        }
    }
}
