package com.itbaizhan.farm_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统访问记录 sys_login_info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_login_info")
public class LoginInfo {
    /** 访问ID */
    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    /** 用户账号 */
    private String userName;

    /** 登录IP地址 */
    private String ipaddr;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录状态（0成功 1失败） */
    private String status;

    /** 提示消息 */
    private String msg;

    /** 访问时间 */
    private LocalDateTime loginTime;
}

