package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.Warehouse;
import com.itbaizhan.farm_warehousing.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 仓库Service
 */
@Service
@Transactional
public class WarehouseService {
    @Autowired
    private WarehouseMapper warehouseMapper;

    /**
     * 分页查询仓库
     * @param page 当前页
     * @param size 每页显示条数
     * @param warehouseName 仓库名称
     * @param warehouseCode 仓库编号
     * @return 仓库分页数据
     */
    public IPage<Warehouse> findWarehousePage(int page, int size, String warehouseName,String warehouseCode) {
        Page<Warehouse> pageObj = new Page<>(page, size);
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(warehouseName)) {
            queryWrapper.like("warehouse_name", warehouseName);
        }
        if (StringUtils.hasText(warehouseCode)) {
            queryWrapper.like("warehouse_code", warehouseCode);
        }
        queryWrapper.orderByDesc("create_time");
        return warehouseMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询仓库详情
     * @param id 仓库ID
     * @return 仓库详情
     */
    public Warehouse findById(Long id) {
        return warehouseMapper.selectById(id);
    }

    /**
     * 新增仓库
     * @param warehouse 仓库信息
     * @return true成功，false失败
     */
    public boolean addWarehouse(Warehouse warehouse) {
        // 检查仓库编号是否存在
        if (StringUtils.hasText(warehouse.getWarehouseCode())) {
            QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_code", warehouse.getWarehouseCode());
            Warehouse existWarehouse = warehouseMapper.selectOne(queryWrapper);
            if (existWarehouse != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }
        // 检查仓库名称是否存在
        if (StringUtils.hasText(warehouse.getWarehouseName())) {
            QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_name", warehouse.getWarehouseName());
            Warehouse existWarehouse = warehouseMapper.selectOne(queryWrapper);
            if (existWarehouse != null){
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        warehouse.setCreateBy(userName);
        warehouse.setUpdateBy(userName);
        warehouse.setCreateTime(LocalDateTime.now());
        warehouse.setUpdateTime(LocalDateTime.now());
        return warehouseMapper.insert(warehouse) > 0;
    }

    /**
     * 修改仓库
     * @param warehouse 仓库信息
     * @return true成功，false失败
     */
    public boolean updateWarehouse(Warehouse warehouse) {
        // 检查仓库编号是否存在(排除自己)
        if (StringUtils.hasText(warehouse.getWarehouseCode())) {
            QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_code", warehouse.getWarehouseCode())
                    .ne("id", warehouse.getId());
            Warehouse existWarehouse = warehouseMapper.selectOne(queryWrapper);
            if (existWarehouse != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }
        // 检查仓库名称是否存在(排除自己)
        if (StringUtils.hasText(warehouse.getWarehouseName())) {
            QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_name", warehouse.getWarehouseName())
                    .ne("id", warehouse.getId());
            Warehouse existWarehouse = warehouseMapper.selectOne(queryWrapper);
            if (existWarehouse != null){
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        warehouse.setUpdateBy(userName);
        warehouse.setUpdateTime(LocalDateTime.now());
        return warehouseMapper.updateById(warehouse) > 0;
    }

    /**
     * 删除仓库
     * @param ids 仓库ID列表
     * @return true成功，false失败
     */
    public boolean deleteWarehouse(List<Long> ids) {
        return warehouseMapper.deleteBatchIds(ids) > 0;
    }
}



