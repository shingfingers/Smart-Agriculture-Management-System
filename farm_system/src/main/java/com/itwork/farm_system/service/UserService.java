package com.itwork.farm_system.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_system.entity.Permission;
import com.itwork.farm_system.entity.Role;
import com.itwork.farm_system.entity.User;
import com.itwork.farm_system.mapper.PermissionMapper;
import com.itwork.farm_system.mapper.RoleMapper;
import com.itwork.farm_system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
//事务，让任务同时完成或者同时失败
@Transactional
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private  PermissionMapper permissionMapper;
    @Autowired
    private PasswordEncoder encoder;
    /**
     * 根据id查询用户
     * @param id 用户ID
     * @return 用户信息，包含角色和权限列表，如果不存在返回null
     */
    public User findById(Long id) {
        // 查询用户基本信息
        User user = userMapper.selectById(id);
        if (user != null) {
            // 查询用户的角色ID列表
            List<Long> roleIds = userMapper.selectUserRoleIds(id);
            if (roleIds != null && !roleIds.isEmpty()) {
                // 查询角色详细信息
                List<Role> roles = roleMapper.selectBatchIds(roleIds);
                // 为每个角色查询权限信息
                for (Role role : roles) {
                    List<Long> permissionIds = roleMapper.selectRolePermissionIds(role.getRoleId());
                    if (permissionIds != null && !permissionIds.isEmpty()) {
                        List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
                        role.setPermissions(permissions);
                    }
                }
                user.setRoles(roles);
            }
        }
        return user;
    }
    public boolean addUser(User user) {
        // 1. 检查用户名是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusException(CodeEnum.SYS_USER_EXIST);
        }

        // 2. 设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setStatus("0");

        // 3. 密码处理
        String rawPassword = StringUtils.hasText(user.getPassword()) ?
                user.getPassword() : "123456";
        user.setPassword(encoder.encode(rawPassword));

        // 4. 执行插入
        return userMapper.insert(user) > 0;
    }

    /*修改用户信息*/
    public boolean updataUser(User user){
        //检查用户名是否重复(排除自己)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName())
                .ne("user_id",user.getUserId());
        User existUser = userMapper.selectOne(queryWrapper);
        //设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        //如果密码为空，则不更新
        if (!StringUtils.hasText(user.getPassword())){
            user.setPassword(null);
        }
        return userMapper.updateById(user)>0;
    }
    //重新设置密码
    public boolean resetPassword(Long userId,String newPassword){
        User user=new User();
        user.setUserId(userId);
        user.setPassword(newPassword);
        user.setPwdUpdateDate(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return  userMapper.updateById(user)>0;
    }
//修改用户状态
    public boolean updataStatus(Long userId,String status){
        User user=new User();
        user.setUserId(userId);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        return  userMapper.updateById(user)>0;
    }

    /**
     * 删除用户
     * @param ids 用户ID列表
     * @return 操作结果
     */
    public boolean deleteUser(List<Long> ids) {
        // 删除用户角色关联
        userMapper.deleteUserRolesByUserIds(ids);
        // 删除用户
        return userMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 分页查询用户
     * @param page 当前页
     * @param size 每页大小
     * @param userName 用户名
     * @param status 状态
     * @return 分页结果
     */
    public IPage<User> findUserPage(int page, int size, String userName, String status) {
        Page<User> pageObj = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(userName)) {
            queryWrapper.like("user_name", userName)
                    .or()
                    .like("nick_name", userName);
        }

        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");
        return userMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 给用户分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 先删除用户的所有角色
        List<Long> userIds = new ArrayList();
        userIds.add(userId);
        userMapper.deleteUserRolesByUserIds(userIds);
        // 如果roleIds不为空，则插入新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            userMapper.insertUserRoles(userId, roleIds);
        }
        return true;
    }
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    public User findByUserName(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", username);
        User admin = userMapper.selectOne(queryWrapper);
        return admin;
    }

    /**
     * 根据用户名查询所有权限
     * @param username 用户名
     * @return 权限列表
     */
    public List<Permission> findAllPermission(String username){

        return userMapper.selectUserPermissions(username);
    }

    /**
     * 根据用户名修改用户个人信息
     * @return 更新结果
     */
    public boolean updateCurrentUserInfo(User user) {
        // 根据用户名查询当前用户信息
        User currentUser = findByUserName(user.getUserName());
        if (currentUser == null) {
            throw new BusException(CodeEnum.PERSONAL_USER_NOT_FOUND);
        }

        // 修改数据
        if (user.getNickName() != null) {
            currentUser.setNickName(user.getNickName());
        }
        if (user.getPhonenumber() != null) {
            currentUser.setPhonenumber(user.getPhonenumber());
        }
        if (user.getEmail() != null) {
            currentUser.setEmail(user.getEmail());
        }
        if (user.getSex() != null) {
            currentUser.setSex(user.getSex());
        }

        // 设置更新信息
        currentUser.setUpdateBy(user.getUserName());
        currentUser.setUpdateTime(LocalDateTime.now());

        // 执行更新
        int result = userMapper.updateById(currentUser);
        return result > 0;
    }

    /**
     * 修改当前登录用户头像
     *
     * @param currentUserName 当前登录用户名
     * @param avatarUrl       头像访问地址
     * @return 更新结果
     */
    public boolean updateCurrentUserAvatar(String currentUserName, String avatarUrl) {
        User currentUser = findByUserName(currentUserName);
        if (currentUser == null) {
            throw new BusException(CodeEnum.PERSONAL_USER_NOT_FOUND);
        }
        currentUser.setAvatar(avatarUrl);
        currentUser.setUpdateBy(currentUserName);
        currentUser.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(currentUser) > 0;
    }
    /**
     * 修改当前用户密码
     * 验证当前密码后更新为新密码
     *
     * @param currentPassword 当前密码
     * @param newPassword 新密码
     * @param confirmPassword 确认新密码
     * @param currentUserName 当前登录用户名
     * @return 修改结果
     */
    public boolean updateCurrentUserPassword(String currentPassword, String newPassword,
                                             String confirmPassword, String currentUserName) {
        // 参数校验
        if (currentPassword == null || currentPassword.trim().isEmpty()
                || newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusException(CodeEnum.PERSONAL_PASSWORD_EMPTY);
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new BusException(CodeEnum.PERSONAL_PASSWORD_NOT_MATCH);
        }

        // 根据用户名查询用户信息
        User user = findByUserName(currentUserName);
        if (user == null) {
            throw new BusException(CodeEnum.PERSONAL_USER_NOT_FOUND);
        }

        // 验证当前密码
        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new BusException(CodeEnum.PERSONAL_CURRENT_PASSWORD_ERROR);
        }

        // 加密新密码
        String encodedNewPassword = encoder.encode(newPassword);

        // 更新密码
        user.setPassword(encodedNewPassword);
        user.setPwdUpdateDate(LocalDateTime.now());
        user.setUpdateBy(currentUserName);
        user.setUpdateTime(LocalDateTime.now());

        // 更新用户密码
        int result = userMapper.updateById(user);
        return result > 0;
    }

}
