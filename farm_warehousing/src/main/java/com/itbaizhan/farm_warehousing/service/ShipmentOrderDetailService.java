package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.ShipmentOrderDetail;
import com.itbaizhan.farm_warehousing.mapper.ShipmentOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 出库单详情服务层
 */
@Service
@Transactional
public class ShipmentOrderDetailService {
    @Autowired
    private ShipmentOrderDetailMapper shipmentOrderDetailMapper;

    /**
     * 分页查询出库单详情
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param shipmentOrderId 出库单id,用于查询特定出库单的详情
     * @return 出库单详情列表
     */
    public IPage<ShipmentOrderDetail> selectShipmentOrderDetailPage(Integer pageNo, Integer pageSize, Long shipmentOrderId){
        QueryWrapper<ShipmentOrderDetail> queryWrapper = new QueryWrapper<>();
        if (shipmentOrderId != null){
            queryWrapper.eq("shipment_order_id",shipmentOrderId);
        }
        queryWrapper.orderByDesc("create_time");
        Page<ShipmentOrderDetail> page = new Page<>(pageNo,pageSize);
        return shipmentOrderDetailMapper.selectPage(page,queryWrapper);
    }

    /**
     * 根据id查询出库单详情
     * @param id 出库单详情id
     * @return 出库单详情
     */
    public ShipmentOrderDetail getShipmentOrderDetailById(Long id){
        return shipmentOrderDetailMapper.selectById(id);
    }

    /**
     * 添加出库单详情
     * @param shipmentOrderDetail 出库单详情信息
     * @return 添加结果
     */
    public boolean addShipmentOrderDetail(ShipmentOrderDetail shipmentOrderDetail){
        String userName = SecurityUtil.getUserName();
        shipmentOrderDetail.setCreateBy(userName);
        shipmentOrderDetail.setUpdateBy(userName);
        shipmentOrderDetail.setCreateTime(LocalDateTime.now());
        shipmentOrderDetail.setUpdateTime(LocalDateTime.now());

        return shipmentOrderDetailMapper.insert(shipmentOrderDetail) > 0;
    }

    /**
     * 修改出库单详情
     * @param shipmentOrderDetail 出库单详情信息
     * @return 修改结果
     */
    public boolean updateShipmentOrderDetail(ShipmentOrderDetail shipmentOrderDetail){
        String userName = SecurityUtil.getUserName();
        shipmentOrderDetail.setUpdateBy(userName);
        shipmentOrderDetail.setUpdateTime(LocalDateTime.now());
        return shipmentOrderDetailMapper.updateById(shipmentOrderDetail) > 0;
    }

    /**
     * 批量删除出库单详情
     * @param ids 出库单详情id列表
     * @return 删除结果
     */
    public boolean deleteShipmentOrderDetail(List<Long> ids){
        return shipmentOrderDetailMapper.deleteBatchIds(ids) > 0;
    }
}

