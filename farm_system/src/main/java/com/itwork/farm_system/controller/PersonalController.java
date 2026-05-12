package com.itwork.farm_system.controller;

import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_system.entity.User;
import com.itwork.farm_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

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

    /**
     * 上传当前用户头像并更新到用户信息
     *
     * @param file 头像文件（表单字段名：file）
     * @param avatar 头像文件（兼容表单字段名：avatar）
     * @return 头像访问地址
     */
    @PostMapping("/avatar")
    public BaseResult<String> uploadAvatar(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        MultipartFile uploadFile = (file != null && !file.isEmpty()) ? file : avatar;
        if (uploadFile == null || uploadFile.isEmpty()) {
            throw new BusException(CodeEnum.PERSONAL_AVATAR_EMPTY);
        }

        String originalFilename = uploadFile.getOriginalFilename();
        String ext = "";
        if (originalFilename != null) {
            int idx = originalFilename.lastIndexOf('.');
            if (idx >= 0 && idx < originalFilename.length() - 1) {
                ext = originalFilename.substring(idx);
            }
        }

        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        Path uploadDir = Path.of(System.getProperty("user.dir"), "upload", "avatar");
        Path targetFile = uploadDir.resolve(filename);

        try {
            Files.createDirectories(uploadDir);
            uploadFile.transferTo(targetFile.toFile());
        } catch (IOException e) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }

        String avatarUrl = "/avatar/" + filename;
        String currentUserName = SecurityUtil.getUserName();
        userService.updateCurrentUserAvatar(currentUserName, avatarUrl);
        return BaseResult.ok(avatarUrl);
    }

}

