package com.itwork.farm_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_system.entity.LoginInfo;
import com.itwork.farm_system.mapper.LoginInfoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 系统访问记录
 */
@Service
@Transactional
public class LoginInfoService {
    @Autowired
    private LoginInfoMapper loginInfoMapper;

    /**
     * 记录用户登录信息
     *
     * @param userName 用户账号，不能为空
     * @param status 登录状态（0：成功，1：失败）
     * @param msg 登录提示消息，如"登录成功"、"用户名或密码错误"等
     * @param request HTTP请求对象，用于获取客户端信息
     */
    public void recordLoginInfo(String userName, String status, String msg, HttpServletRequest request) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserName(userName);
        loginInfo.setIpaddr(getClientIp(request));
        loginInfo.setBrowser(getBrowser(request));
        loginInfo.setOs(getOs(request));
        loginInfo.setStatus(status);
        loginInfo.setMsg(msg);
        loginInfo.setLoginTime(LocalDateTime.now());

        loginInfoMapper.insert(loginInfo);
    }

    /**
     * 获取客户端真实IP地址
     * @param request HTTP请求对象
     * @return 客户端IP地址，如果无法获取则返回请求的远程地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 解析并获取客户端浏览器类型
     * @param request HTTP请求对象
     * @return 浏览器类型名称，包括：
     *     Chrome - Google Chrome浏览器
     *     Firefox - Mozilla Firefox浏览器
     *     Safari - Apple Safari浏览器
     *     Edge - Microsoft Edge浏览器
     *     Other - 其他浏览器
     *     Unknown - 无法识别的浏览器
     */
    private String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        if (userAgent.contains("Edge")) return "Edge";
        return "Other";
    }

    /**
     * 解析并获取客户端操作系统类型
     * @param request HTTP请求对象
     * @return 操作系统类型名称，包括：
     *     Windows - Microsoft Windows系统
     *     macOS - Apple macOS系统
     *     Linux - Linux系统
     *     Android - Google Android系统
     *     iOS - Apple iOS系统
     *     Other - 其他操作系统
     *     Unknown - 无法识别的操作系统
     */
    private String getOs(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac")) return "macOS";
        if (userAgent.contains("Linux")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iPhone")) return "iOS";
        return "Other";
    }

    /**
     * 根据ID查询登录信息
     *
     * @param id 访问记录ID
     * @return 登录信息，如果不存在返回null
     */
    public LoginInfo findById(Long id) {
        return loginInfoMapper.selectById(id);
    }

    /**
     * 分页查询登录信息
     *
     * @param page 当前页码，从1开始
     * @param size 每页大小，必须大于0
     * @param userName 用户名，可选，支持模糊查询
     * @param status 登录状态，可选，0-成功 1-失败
     * @param ipaddr IP地址，可选，支持模糊查询
     * @return 分页结果
     */
    public IPage<LoginInfo> findLoginInfoPage(int page, int size, String userName, String status, String ipaddr) {
        Page<LoginInfo> pageObj = new Page<>(page, size);
        QueryWrapper<LoginInfo> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(userName)) {
            queryWrapper.like("user_name", userName);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        if (StringUtils.hasText(ipaddr)) {
            queryWrapper.like("ipaddr", ipaddr);
        }

        queryWrapper.orderByDesc("login_time");
        return loginInfoMapper.selectPage(pageObj, queryWrapper);
    }
}
