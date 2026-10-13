package com.agile.taskboard.service;

import com.agile.taskboard.dto.LoginDTO;
import com.agile.taskboard.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginDTO dto);
}
