package com.itwork.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_plant.entity.Material;
import com.itwork.farm_plant.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 农资信息Service
 */
@Service
@Transactional
public class MaterialService {
    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 分页查询农资信息
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param materialName 农资名称
     * @param materialTypeId 农资类别ID
     * @return 农资信息分页数据
     */
    public IPage<Material> findMaterialPage(int page, int size, String materialName, Long materialTypeId) {
        Page<Material> pageObj = new Page<>(page, size);
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(materialName)) {
            queryWrapper.like("material_name", materialName);
        }
        if (materialTypeId != null) {
            queryWrapper.eq("material_type_id", materialTypeId);
        }
        queryWrapper.orderByDesc("create_time");
        return materialMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询农资信息详情
     *
     * @param id 农资信息ID
     * @return 农资信息详情
     */
    public Material findById(Long id) {
        return materialMapper.selectById(id);
    }

    /**
     * 新增农资信息
     *
     * @param material 农资信息
     * @return true成功，false失败
     */
    public boolean addMaterial(Material material) {
        if (material == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(material.getMaterialCode())) {
            QueryWrapper<Material> wrapper = new QueryWrapper<>();
            wrapper.eq("material_code", material.getMaterialCode());
            Material existMaterial = materialMapper.selectOne(wrapper);
            if (existMaterial != null) {
                throw new BusException(CodeEnum.PLANT_CODE_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        material.setCreateBy(userName);
        material.setUpdateBy(userName);
        material.setCreateTime(LocalDateTime.now());
        material.setUpdateTime(LocalDateTime.now());
        return materialMapper.insert(material) > 0;
    }

    /**
     * 修改农资信息
     *
     * @param material 农资信息
     * @return true成功，false失败
     */
    public boolean updateMaterial(Material material) {
        if (material == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(material.getMaterialCode())) {
            QueryWrapper<Material> wrapper = new QueryWrapper<>();
            wrapper.eq("material_code", material.getMaterialCode())
                    .ne("material_id", material.getMaterialId());
            Material existMaterial = materialMapper.selectOne(wrapper);
            if (existMaterial != null) {
                throw new BusException(CodeEnum.PLANT_CODE_EXIST);
            }
        }
        material.setUpdateBy(SecurityUtil.getUserName());
        material.setUpdateTime(LocalDateTime.now());
        return materialMapper.updateById(material) > 0;
    }

    /**
     * 删除农资信息
     *
     * @param ids 农资信息ID列表
     * @return true成功，false失败
     */
    public boolean deleteMaterial(List<Long> ids) {
        return materialMapper.deleteBatchIds(ids) > 0;
    }
}

