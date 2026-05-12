package com.itwork.farm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwork.farm_system.entity.Role;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据角色ID查询权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
//    // 根据单个角色ID查权限ID列表
 List<Long> selectRolePermissionIds(@Param("roleId") Long roleId);

    /**
     * 根据角色Id删除角色与权限关联
     * @param roleIds 角色id列表
     */
    void deleteRolePermissionsByRoleIds(@Param("roleIds")List<Long> roleIds) ;

    /**
     *
     */
    void deleteUserRolesByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据角色ID查询用户ID列表
     * @param roleId 角色id
     * @return 用户ID列表
     */
    List<Long> selectRoleUserIds(@Param("roleId") Long roleId);

    /**
     * 检查用户角色关联是否存在
     * @param roleId 角色id
     * @param userId 用户id
     * @return 关联数量
     */
    int countUserRoleExists(@Param("roleId") Long roleId,@Param("userId") Long userId);
    /**
     * 插入用户角色关联
     * @param roleId 角色id
     * @param userId 用户id
     * @return 关联数量
     */
     void insertUserRole(@Param("roleId") Long roleId,@Param("userId") Long userId);
    /**
     * 根据角色id和用户id列表删除用户角色关联
     * @param roleId 角色id
     * @param userIds 用户id列表
     * @return 关联数量
     */
    void deleteUserRolesByRoleIdAndUserIds(@Param("roleId")Long roleId,@Param("userIds") List<Long> userIds);

    /**
     * 插入角色与权限的关联
     * @param roleId 角色id
     * @param permissionIds 权限列表
     */
          void   insertRolePermissions(@Param("roleId")Long roleId,@Param("permissionIds") List<Long> permissionIds);
}
