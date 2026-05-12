package com.itbaizhan.farm_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_system.entity.Permission;
import com.itbaizhan.farm_system.mapper.PermissionMapper;
import com.itbaizhan.farm_system.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 分页查询权限
     * @param page 当前页码
     * @param size 每页大小
     * @param permissionName 权限名称（可选）
     * @return 权限分页结果
     */
    public IPage<Permission> findPermissionPage(int page, int size, String permissionName) {
        Page<Permission> pageObj = new Page<>(page, size);
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(permissionName)) {
            queryWrapper.like("permission_name", permissionName);
        }
        queryWrapper.orderByAsc("permission_id");
        return permissionMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询权限
     * @param id 权限ID
     * @return 权限信息，如果不存在返回null
     */
    public Permission findById(Long id) {
        return permissionMapper.selectById(id);
    }

    /**
     * 获取权限下拉列表
     * @return 所有权限列表
     */
    public List<Permission> getPermissionSelectList() {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("permission_id");
        queryWrapper.select("permission_id", "permission_name");
        return permissionMapper.selectList(queryWrapper);
    }

    /**
     * 新增权限
     * @param permission 权限信息
     * @return true-成功，false-失败
     */
    public boolean addPermission(Permission permission) {
        // 检查权限名称是否存在
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_name", permission.getPermissionName());
        Permission existPermission = permissionMapper.selectOne(queryWrapper);
        if (existPermission != null) {
            throw new BusException(CodeEnum.SYS_PERMISSION_EXIST);
        }

        permission.setCreateTime(LocalDateTime.now());

        return permissionMapper.insert(permission) > 0;
    }

    /**
     * 修改权限
     * @param permission 权限信息
     * @return true-成功，false-失败
     */
    public boolean updatePermission(Permission permission) {
        // 检查权限名称是否重复（排除自己）
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_name", permission.getPermissionName())
                .ne("permission_id", permission.getPermissionId());
        Permission existPermission = permissionMapper.selectOne(queryWrapper);
        if (existPermission != null) {
            throw new BusException(CodeEnum.SYS_PERMISSION_EXIST);
        }

        permission.setUpdateTime(LocalDateTime.now());

        return permissionMapper.updateById(permission) > 0;
    }

    /**
     * 删除权限
     * @param ids 权限ID列表
     * @return true-成功，false-失败
     */
    public boolean deletePermission(List<Long> ids) {
        // 删除角色权限关联
        permissionMapper.deleteRolePermissionsByPermissionIds(ids);
        // 删除权限
        return permissionMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 获取所有权限
     * @return 所有权限列表
     */
    public List<Permission> getAllPermissions() {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("permission_id");
        return permissionMapper.selectList(queryWrapper);
    }
}
