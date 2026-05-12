package com.itwork.farm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwork.farm_system.entity.Permission;
import com.itwork.farm_system.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//定义数据库操作方法，只声明不实现
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户ID列表批量删除用户角色关联
     * @param userIds 用户ID列表
     */
    void deleteUserRolesByUserIds(@Param("userIds") List<Long> userIds);
    /*根据用户id查询角色id列表*/
    List<Long> selectUserRoleIds(@Param("userId") Long userId );

    /**
     * 批量插入用户角色关联
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    /**
     * 根据用户名查询权限列表
     * @param username 用户名
     * @return 权限列表
     */
    List<Permission> selectUserPermissions(String username);
}
