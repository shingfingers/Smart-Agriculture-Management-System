package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.Land;
import com.itbaizhan.farm_plant.mapper.LandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地块Service
 */
@Service
@Transactional
public class LandService {
    @Autowired
    private LandMapper landMapper;

    /**
     * 分页查询地块
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param landName 地块名称
     * @param landType 地块类型
     * @return 地块分页数据
     */
    public IPage<Land> findLandPage(int page, int size, String landName, String landType) {
        Page<Land> pageObj = new Page<>(page, size);
        QueryWrapper<Land> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(landName)) {
            queryWrapper.like("land_name", landName);
        }
        if (StringUtils.hasText(landType)) {
            queryWrapper.eq("land_type", landType);
        }
        queryWrapper.orderByDesc("create_time");
        return landMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询地块详情
     *
     * @param id 地块ID
     * @return 地块详情
     */
    public Land findById(Long id) {
        return landMapper.selectById(id);
    }

    /**
     * 新增地块
     *
     * @param land 地块信息
     * @return true成功，false失败
     */
    public boolean addLand(Land land) {
        if (land == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(land.getLandName())) {
            QueryWrapper<Land> wrapper = new QueryWrapper<>();
            wrapper.eq("land_name", land.getLandName());
            Land existLand = landMapper.selectOne(wrapper);
            if (existLand != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        land.setCreateBy(userName);
        land.setUpdateBy(userName);
        land.setCreateTime(LocalDateTime.now());
        land.setUpdateTime(LocalDateTime.now());
        return landMapper.insert(land) > 0;
    }

    /**
     * 修改地块
     *
     * @param land 地块信息
     * @return true成功，false失败
     */
    public boolean updateLand(Land land) {
        if (land == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(land.getLandName())) {
            QueryWrapper<Land> wrapper = new QueryWrapper<>();
            wrapper.eq("land_name", land.getLandName())
                    .ne("land_id", land.getLandId());
            Land existLand = landMapper.selectOne(wrapper);
            if (existLand != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        land.setUpdateBy(SecurityUtil.getUserName());
        land.setUpdateTime(LocalDateTime.now());
        return landMapper.updateById(land) > 0;
    }

    /**
     * 删除地块
     *
     * @param ids 地块ID列表
     * @return true成功，false失败
     */
    public boolean deleteLand(List<Long> ids) {
        return landMapper.deleteBatchIds(ids) > 0;
    }
}

