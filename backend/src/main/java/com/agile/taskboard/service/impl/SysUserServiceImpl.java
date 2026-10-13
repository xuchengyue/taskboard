package com.agile.taskboard.service.impl;

import com.agile.taskboard.common.BusinessException;
import com.agile.taskboard.entity.SysUser;
import com.agile.taskboard.mapper.SysUserMapper;
import com.agile.taskboard.service.SysUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<SysUser> listAll() {
        return sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery().orderByAsc(SysUser::getId));
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    @Override
    public SysUser createUser(SysUser user) {
        long count = sysUserMapper.selectCount(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, user.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        // 新建账号统一给个默认密码，让用户自己首次登录后再改
        user.setPassword(passwordEncoder.encode("123456"));
        user.setStatus(1);
        sysUserMapper.insert(user);
        return user;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = getById(id);
        user.setStatus(status);
        sysUserMapper.updateById(user);
    }

    @Override
    public void resetPassword(Long id) {
        SysUser user = getById(id);
        user.setPassword(passwordEncoder.encode("123456"));
        sysUserMapper.updateById(user);
    }
}
