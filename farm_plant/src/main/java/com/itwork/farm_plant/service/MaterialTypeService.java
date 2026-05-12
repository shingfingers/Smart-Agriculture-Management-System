package com.itwork.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_plant.entity.MaterialType;
import com.itwork.farm_plant.mapper.MaterialTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 农资类别Service
 */
@Service
@Transactional
public class MaterialTypeService {
    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    /**
     * 分页查询农资类别
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param materialTypeName 农资类别名称
     * @return 农资类别分页数据
     */
    public IPage<MaterialType> findMaterialTypePage(int page, int size, String materialTypeName) {
        Page<MaterialType> pageObj = new Page<>(page, size);
        QueryWrapper<MaterialType> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(materialTypeName)) {
            queryWrapper.like("material_type_name", materialTypeName);
        }
        queryWrapper.orderByAsc("order_num");
        return materialTypeMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询农资类别详情
     *
     * @param id 农资类别ID
     * @return 农资类别详情
     */
    public MaterialType findById(Long id) {
        return materialTypeMapper.selectById(id);
    }

    /**
     * 新增农资类别
     *
     * @param materialType 农资类别信息
     * @return true成功，false失败
     */
    public boolean addMaterialType(MaterialType materialType) {
        if (materialType == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(materialType.getMaterialTypeName())) {
            QueryWrapper<MaterialType> wrapper = new QueryWrapper<>();
            wrapper.eq("material_type_name", materialType.getMaterialTypeName());
            MaterialType existType = materialTypeMapper.selectOne(wrapper);
            if (existType != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        materialType.setCreateBy(userName);
        materialType.setUpdateBy(userName);
        materialType.setCreateTime(LocalDateTime.now());
        materialType.setUpdateTime(LocalDateTime.now());
        return materialTypeMapper.insert(materialType) > 0;
    }

    /**
     * 修改农资类别
     *
     * @param materialType 农资类别信息
     * @return true成功，false失败
     */
    public boolean updateMaterialType(MaterialType materialType) {
        if (materialType == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(materialType.getMaterialTypeName())) {
            QueryWrapper<MaterialType> wrapper = new QueryWrapper<>();
            wrapper.eq("material_type_name", materialType.getMaterialTypeName())
                    .ne("material_type_id", materialType.getMaterialTypeId());
            MaterialType existType = materialTypeMapper.selectOne(wrapper);
            if (existType != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        materialType.setUpdateBy(SecurityUtil.getUserName());
        materialType.setUpdateTime(LocalDateTime.now());
        return materialTypeMapper.updateById(materialType) > 0;
    }

    /**
     * 删除农资类别
     *
     * @param ids 农资类别ID列表
     * @return true成功，false失败
     */
    public boolean deleteMaterialType(List<Long> ids) {
        return materialTypeMapper.deleteBatchIds(ids) > 0;
    }
}

