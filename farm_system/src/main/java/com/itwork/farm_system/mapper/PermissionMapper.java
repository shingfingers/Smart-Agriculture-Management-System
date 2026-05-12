package com.itwork.farm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwork.farm_system.entity.Permission;  // 需要导入实体类
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    void deleteRolePermissionsByPermissionIds(@Param("permissionIds") List<Long> permissionIds);
}