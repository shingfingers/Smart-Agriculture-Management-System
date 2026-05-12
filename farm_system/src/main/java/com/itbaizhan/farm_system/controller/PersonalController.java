package com.itbaizhan.farm_system.controller;

import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_system.entity.User;
import com.itbaizhan.farm_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 个人中心控制器
 * 提供用户个人信息管理相关接口
 */
@RestController
@RequestMapping("/personal")
public class PersonalController {

    @Autowired
    private UserService userService;

    /**
     * 查询当前用户的个人信息
     * 返回用户名、昵称、手机号、邮箱、角色、头像、性别等信息
     *
     * @return 用户的个人信息
     */
    @GetMapping("/info")
    public BaseResult<User> getUserInfo() {
        // 获取当前登录用户名
        String currentUserName = SecurityUtil.getUserName();
        // 根据用户名查询用户信息
        User user = userService.findByUserName(currentUserName);
        // 返回用户信息
        return BaseResult.ok(user);
    }
    /**
     * 修改当前用户的个人信息
     * @return 操作结果
     */
    @PutMapping("/update")
    public BaseResult updateUserInfo(@RequestBody User user) {
        // 获取当前登录用户名
        user.setUserName(SecurityUtil.getUserName());
        // 调用service层处理业务逻辑
        userService.updateCurrentUserInfo(user);
        return BaseResult.ok();
    }

}

