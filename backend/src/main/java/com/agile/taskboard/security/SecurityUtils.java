package com.agile.taskboard.security;

import com.agile.taskboard.common.BusinessException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static LoginUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LoginUser loginUser) {
            return loginUser;
        }
        throw new BusinessException(401, "未登录或登录已过期");
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public static String getCurrentRole() {
        return getCurrentUser().getUser().getRole();
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(getCurrentRole());
    }

    public static boolean isPM() {
        return "PM".equals(getCurrentRole());
    }
}
