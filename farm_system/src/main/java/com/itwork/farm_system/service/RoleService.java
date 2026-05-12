package com.itwork.farm_system.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_system.entity.Permission;
import com.itwork.farm_system.entity.Role;
import com.itwork.farm_system.entity.User;
import com.itwork.farm_system.mapper.PermissionMapper;
import com.itwork.farm_system.mapper.RoleMapper;
import com.itwork.farm_system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
//让一系列数据库操作要么全部成功，要么全部失败
@Transactional
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 分页查询角色
     * @param page 当前页码
     * @param size 每页大小
     * @param roleName 角色名称（可选）
     * @param status 角色状态（可选）
     * @return 角色分页结果
     */
    public IPage<Role> findRolePage(int page, int size, String roleName, String status) {
        Page<Role> pageObj = new Page<>(page, size);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(roleName)) {
            queryWrapper.like("role_name", roleName);
        }

        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }

        queryWrapper.orderByAsc("role_sort");

        return roleMapper.selectPage(pageObj, queryWrapper);
    }
      /**
     * 根据id查询角色（包含权限信息）
     * @param id 角色ID
     * @return 角色信息，包含权限列表，如果不存在返回null
     */
    public Role findById(Long id) {
        // 查询角色基本信息
        Role role = roleMapper.selectById(id);
        if (role != null) {
            // 查询角色的权限ID列表
            List<Long> permissionIds = roleMapper.selectRolePermissionIds(id);
            if (permissionIds != null && !permissionIds.isEmpty()) {
                // 查询权限详细信息
                List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
                role.setPermissions(permissions);
            }
        }
        return role;
    }

    /**
     * 新增角色
     * @param role 角色信息
     * @return true-成功，false-失败
     */
    public boolean addRole(Role role) {
        // 检查角色名称是否存在
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", role.getRoleName());
        Role existRole = roleMapper.selectOne(queryWrapper);
        if (existRole != null) {
            throw new BusException(CodeEnum.SYS_ROLE_EXIST);
        }

        role.setCreateTime(LocalDateTime.now());
        role.setStatus("0"); // 默认正常状态

        // 添加创建人、创建时间、修改人、修改时间
        String userName = SecurityUtil.getUserName();
        role.setCreateBy(userName);
        role.setUpdateBy(userName);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        return roleMapper.insert(role) > 0;
    }

    /**
     * 修改角色
     * @param role 角色信息
     * @return true-成功，false-失败
     */
    public boolean updateRole(Role role) {
        // 检查角色名称是否重复（排除自己）
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", role.getRoleName())
                .ne("role_id", role.getRoleId());
        Role existRole = roleMapper.selectOne(queryWrapper);
        if (existRole != null) {
            throw new BusException(CodeEnum.SYS_ROLE_EXIST);
        }
        role.setUpdateTime(LocalDateTime.now());
        return roleMapper.updateById(role) > 0;
    }

    /**
     * 删除角色
     * @param ids 角色ID列表
     * @return true-成功，false-失败
     */
    public boolean deleteRole(List<Long> ids) {
        // 删除角色权限关联
        roleMapper.deleteRolePermissionsByRoleIds(ids);
        // 删除用户角色关联
        roleMapper.deleteUserRolesByRoleIds(ids);

        return roleMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 获取角色选择框列表
     * @return 正常状态的角色列表
     */
    public List<Role> getRoleSelectList() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0"); // 只查询正常状态的角色
        queryWrapper.orderByAsc("role_sort");
        queryWrapper.select("role_id", "role_name");

        return roleMapper.selectList(queryWrapper);
    }

    /**
     * 修改角色状态
     * @param roleId 角色ID
     * @param status 角色状态
     * @return true-成功，false-失败
     */
    public boolean updateStatus(Long roleId, String status) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setStatus(status);
        role.setUpdateTime(LocalDateTime.now());

        return roleMapper.updateById(role) > 0;
    }

    /**
     * 查询已分配该角色的用户列表
     * @param roleId 角色ID
     * @param page 当前页
     * @param size 每页显示条数
     * @param userName 用户名称
     * @return 已分配该角色的用户分页数据
     */
    public IPage<User> getAssignedUsers(Long roleId, int page, int size, String userName) {
        Page<User> pageObj = new Page<>(page, size);
        // 查询已分配该角色的用户id
        List<Long> userIds = roleMapper.selectRoleUserIds(roleId);
        if (userIds.isEmpty()) {
            return pageObj; // 返回空页面
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        if (StringUtils.hasText(userName)) {
            queryWrapper.like("user_name", userName)
                    .or()
                    .like("nick_name",userName);
        }
        queryWrapper.eq("status", "0"); // 只查询正常状态的用户
        queryWrapper.orderByDesc("create_time");
        return userMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 查询未分配该角色的用户列表
     * @param roleId 角色ID
     * @param page 当前页
     * @param size 每页显示条数
     * @param userName 用户名称
     * @return 未分配该角色的用户分页数据
     */
    public IPage<User> getUnAssignedUsers(Long roleId,int page, int size, String userName) {
        Page<User> pageObj = new Page<>(page, size);
        // 查询已分配该角色的用户id
        List<Long> userIds = roleMapper.selectRoleUserIds(roleId);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!userIds.isEmpty()) {
            queryWrapper.notIn("user_id", userIds);
        }
        if (StringUtils.hasText(userName)) {
            queryWrapper.like("user_name", userName)
                    .or()
                    .like("nick_name",userName);
        }
        queryWrapper.eq("status", "0"); // 只查询正常状态的用户
        queryWrapper.orderByDesc("create_time");
        return userMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 批量选择用户分配角色
     * @param roleId 角色ID
     * @param userIds 用户ID列表
     * @return true-成功，false-失败
     */
    public boolean assignRoleToUsers(Long roleId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        for (Long userId : userIds) {
            // 检查是否已存在关联
            int count = roleMapper.countUserRoleExists(roleId, userId);
            if (count == 0) {
                // 不存在则插入
                roleMapper.insertUserRole(roleId, userId);
            }
        }

        return true;
    }

    /**
     * 批量选择用户取消角色
     * @param roleId 角色ID
     * @param userIds 用户ID列表
     * @return true-成功，false-失败
     */
    public boolean cancelRoleFromUsers(Long roleId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        roleMapper.deleteUserRolesByRoleIdAndUserIds(roleId, userIds);
        return true;
    }


    /**
     * 给角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return true-成功，false-失败
     */
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除角色的所有权限
        List<Long> roleIds = new ArrayList();
        roleIds.add(roleId);
        roleMapper.deleteRolePermissionsByRoleIds(roleIds);
        // 如果permissionIds不为空，则插入新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.insertRolePermissions(roleId, permissionIds);
        }
        return true;
    }

    /**
     * 获取角色权限对象列表
     * @param roleId 角色ID
     * @return 权限对象列表
     */
    public List<Permission> getRolePermissions(Long roleId) {
        List<Long> permissionIds = roleMapper.selectRolePermissionIds(roleId);
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Arrays.asList();
        }
        return permissionMapper.selectBatchIds(permissionIds);
    }

    /**
     * 查询未分配该角色的用户列表
     * @param roleId 角色ID
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param userName 用户名称（可选）
     * @return 未分配该角色的用户分页数据
     */
    public IPage<User> getUnassignedUsers(Long roleId, int pageNum, int pageSize, String userName) {
        Page<User> pageObj = new Page<>(pageNum, pageSize);
        // 查询已分配该角色的用户id
        List<Long> userIds = roleMapper.selectRoleUserIds(roleId);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!userIds.isEmpty()) {
            queryWrapper.notIn("user_id", userIds);
        }
        if (StringUtils.hasText(userName)) {
            queryWrapper.and(wrapper ->
                    wrapper.like("user_name", userName)
                            .or()
                            .like("nick_name", userName)
            );
        }
        queryWrapper.eq("status", "0"); // 只查询正常状态的用户
        queryWrapper.orderByDesc("create_time");
        return userMapper.selectPage(pageObj, queryWrapper);
    }
}

