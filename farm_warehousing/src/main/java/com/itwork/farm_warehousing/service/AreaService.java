package com.itwork.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_warehousing.entity.Area;
import com.itwork.farm_warehousing.mapper.AreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库区Service
 */
@Service
@Transactional
public class AreaService {
    @Autowired
    private AreaMapper areaMapper;

    /**
     * 分页查询库区
     * @param page 当前页
     * @param size 每页显示条数
     * @param areaName 库区名称
     * @param areaCode 库区编码
     * @param warehouseId 仓库ID
     * @return 库区分页数据
     */
    public IPage<Area> findAreaPage(int page, int size, String areaName, String areaCode,Long warehouseId) {
        Page<Area> pageObj = new Page<>(page, size);
        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(areaName)) {
            queryWrapper.like("area_name", areaName);
        }
        if (StringUtils.hasText(areaCode)) {
            queryWrapper.like("area_code", areaCode);
        }
        if (warehouseId != null) {
            queryWrapper.eq("warehouse_id", warehouseId);
        }
        queryWrapper.orderByDesc("create_time");
        return areaMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询库区详情
     * @param id 库区ID
     * @return 库区详情
     */
    public Area findById(Long id) {
        return areaMapper.selectById(id);
    }

    /**
     * 新增库区
     * @param area 库区信息
     * @return true成功，false失败
     */
    public boolean addArea(Area area) {
        // 检查库区编码是否存在
        if (StringUtils.hasText(area.getAreaCode())) {
            QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("area_code", area.getAreaCode());
            Area existArea = areaMapper.selectOne(queryWrapper);
            if (existArea != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }
        // 检查库区名称是否存在（同一仓库下）
        if (StringUtils.hasText(area.getAreaName())) {
            QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_id", area.getWarehouseId())
                    .eq("area_name", area.getAreaName());
            Area existArea = areaMapper.selectOne(queryWrapper);
            if (existArea != null){
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        area.setCreateBy(userName);
        area.setUpdateBy(userName);
        area.setCreateTime(LocalDateTime.now());
        area.setUpdateTime(LocalDateTime.now());
        return areaMapper.insert(area) > 0;
    }

    /**
     * 修改库区
     * @param area 库区信息
     * @return true成功，false失败
     */
    public boolean updateArea(Area area) {
        // 检查库区编码是否存在(排除自己)
        if (StringUtils.hasText(area.getAreaCode())) {
            QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("area_code", area.getAreaCode())
                    .ne("id", area.getId());
            Area existArea = areaMapper.selectOne(queryWrapper);
            if (existArea != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }

        // 检查库区名称是否存在（同一仓库下）
        if (StringUtils.hasText(area.getAreaName())) {
            QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_id", area.getWarehouseId())
                    .eq("area_name", area.getAreaName())
                    .ne("id", area.getId());
            Area existArea = areaMapper.selectOne(queryWrapper);
            if (existArea != null){
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        area.setUpdateBy(userName);
        area.setUpdateTime(LocalDateTime.now());
        return areaMapper.updateById(area) > 0;
    }

    /**
     * 删除库区
     * @param ids 库区ID列表
     * @return true成功，false失败
     */
    public boolean deleteArea(List<Long> ids) {
        return areaMapper.deleteBatchIds(ids) > 0;
    }
}



