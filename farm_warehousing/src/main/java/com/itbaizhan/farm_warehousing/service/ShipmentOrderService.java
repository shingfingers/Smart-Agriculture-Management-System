package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.OrderNumberUtil;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.*;
import com.itbaizhan.farm_warehousing.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单服务层
 */
@Service
@Transactional
public class ShipmentOrderService {
    @Autowired
    private ShipmentOrderMapper shipmentOrderMapper;
    @Autowired
    private ShipmentOrderDetailMapper shipmentOrderDetailMapper;
    @Autowired
    private OrderNumberUtil orderNumberUtil;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;
    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;

    /**
     * 查询出库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param shipmentOrderNo 出库单号，可选
     * @param shipmentOrderStatus 出库单状态，可选（0待出库 1已出库）
     * @return 出库单分页列表
     */
    public IPage<ShipmentOrder> selectShipmentOrderPage(Integer pageNo, Integer pageSize, String shipmentOrderNo, Integer shipmentOrderStatus){
        Page<ShipmentOrder> page = new Page<>(pageNo,pageSize);
        QueryWrapper<ShipmentOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(shipmentOrderNo)){
            queryWrapper.like("shipment_order_no",shipmentOrderNo);
        }
        if (shipmentOrderStatus != null){
            queryWrapper.eq("shipment_order_status",shipmentOrderStatus);
        }
        queryWrapper.orderByDesc("create_time");
        return shipmentOrderMapper.selectPage(page,queryWrapper);
    }

    /**
     * 根据ID查询出库单
     * @param id 出库单ID
     * @return 出库单详细信息，包含出库单详情列表
     */
    public ShipmentOrder getShipmentOrderById(Long id){
        ShipmentOrder shipmentOrder = shipmentOrderMapper.selectById(id);
        if (shipmentOrder != null){
            QueryWrapper<ShipmentOrderDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shipment_order_id",id);
            List<ShipmentOrderDetail> shipmentOrderDetails = shipmentOrderDetailMapper.selectList(queryWrapper);
            shipmentOrder.setDetailList(shipmentOrderDetails);
        }
        return shipmentOrder;
    }

    /**
     * 添加出库单
     * @param shipmentOrder 出库单信息
     * @return 出库单号
     */
    public String addShipmentOrder(ShipmentOrder shipmentOrder){
        String userName = SecurityUtil.getUserName();
        shipmentOrder.setCreateBy(userName);
        shipmentOrder.setUpdateBy(userName);
        shipmentOrder.setCreateTime(LocalDateTime.now());
        shipmentOrder.setUpdateTime(LocalDateTime.now());

        // 设置初始状态
        shipmentOrder.setShipmentOrderStatus(0); // 待出库
        shipmentOrder.setShipmentOrderType(1); // 出库类型
        // 生成出库单号
        String shipmentOrderNo = "CK" + orderNumberUtil.generateOrderNumber();
        shipmentOrder.setShipmentOrderNo(shipmentOrderNo);
        shipmentOrderMapper.insert(shipmentOrder);
        return shipmentOrderNo;
    }

    /**
     * 修改出库单
     * @param shipmentOrder 出库单信息
     * @return 修改结果
     */
    public boolean updateShipmentOrder(ShipmentOrder shipmentOrder){
        if (shipmentOrder.getShipmentOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        String userName = SecurityUtil.getUserName();
        shipmentOrder.setUpdateBy(userName);
        shipmentOrder.setUpdateTime(LocalDateTime.now());
        return shipmentOrderMapper.updateById(shipmentOrder) > 0;
    }

    /**
     * 删除出库单
     * @param id 出库单id
     * @return 操作结果
     */
    public boolean deleteShipmentOrder(Long id){
        // 检查出库单状态
        ShipmentOrder shipmentOrder = shipmentOrderMapper.selectById(id);
        if (shipmentOrder.getShipmentOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        // 删除出库单详情
        QueryWrapper<ShipmentOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shipment_order_id",id);
        shipmentOrderDetailMapper.delete(queryWrapper);
        // 删除出库单
        return shipmentOrderMapper.deleteById(id) > 0;
    }

    /**
     * 完成出库操作
     * 复杂的出库流程：校验库存、更新库存、记录流水等
     * @param id 出库单id
     * @return 操作结果
     */
    public boolean completeShipment(Long id){
        // 检验出库单
        ShipmentOrder shipmentOrder = this.getShipmentOrderById(id);
        if (shipmentOrder == null){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_NOT_EXIST);
        }
        if (shipmentOrder.getShipmentOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        // 合并出库单详情
        mergeShipmentOrderDetail(shipmentOrder);
        // 校验出库单详情
        validateShipmentOrderDetails(shipmentOrder);
        // 更新库存数据
        updateInventory(shipmentOrder);
        // 更新库存详情
        updateInventoryDetail(shipmentOrder);
        // 保存库存历史记录
        saveInventoryHistory(shipmentOrder);
        // 更新出库单状态
        shipmentOrder.setShipmentOrderStatus(1); // 已出库
        String userName = SecurityUtil.getUserName();
        shipmentOrder.setUpdateBy(userName);
        shipmentOrder.setUpdateTime(LocalDateTime.now());
        return shipmentOrderMapper.updateById(shipmentOrder) > 0;
    }

    /**
     * 合并出库单详情，按仓库+库区+SKU+库存详情ID
     * @param shipmentOrder 出库单
     */
    private void mergeShipmentOrderDetail(ShipmentOrder shipmentOrder){
        // 创建一个Map， key为仓库+库区+SKU+库存详情ID,value为出库单详情
        Map<String,ShipmentOrderDetail> detailMap = new HashMap<>();
        for (ShipmentOrderDetail orderDetail : shipmentOrder.getDetailList()) {
            String key = orderDetail.getWarehouseId() + "_" + orderDetail.getAreaId() + "_"
                    + orderDetail.getSkuId() + "_" + orderDetail.getInventoryDetailId();
            if (detailMap.containsKey(key)){
                // 如果已存在相同物料，则累加数量
                detailMap.get(key).setQuantity(detailMap.get(key).getQuantity().add(orderDetail.getQuantity()));
            }else {
                // 如果不存在相同物料，则创建新的物料
                detailMap.put(key,orderDetail);
            }
        }
        shipmentOrder.setDetailList(new ArrayList<>(detailMap.values()));
    }

    /**
     * 校验出库单详情，检查库存是否充足
     * @param shipmentOrder 出库单
     */
    private void validateShipmentOrderDetails(ShipmentOrder shipmentOrder){
        // 是否有详情
        List<ShipmentOrderDetail> detailList = shipmentOrder.getDetailList();
        if (detailList == null || detailList.isEmpty()){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_DETAIL_ISNULL);
        }

        // 遍历详情，检查库存是否充足
        for (ShipmentOrderDetail orderDetail : detailList) {
            // 检查库存是否充足
            QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
            inventoryQueryWrapper.eq("warehouse_id",orderDetail.getWarehouseId())
                    .eq("area_id",orderDetail.getAreaId())
                    .eq("sku_id",orderDetail.getSkuId());
            Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
            if (inventory == null || inventory.getQuantity().compareTo(orderDetail.getQuantity()) < 0){
                throw new BusException(CodeEnum.WAREHOUSING_INSUFFICIENT);
            }
            // 检查库存详情是否充足，出库时会选择出库哪个库存详情的库存，所以要检查库存详情是否充足
            InventoryDetail inventoryDetail = inventoryDetailMapper.selectById(orderDetail.getInventoryDetailId());
            if (inventoryDetail == null || inventoryDetail.getRemainQuantity().compareTo(orderDetail.getQuantity()) < 0){
                throw new BusException(CodeEnum.WAREHOUSING_INSUFFICIENT);
            }
        }
    }

    /**
     * 更新库存
     * @param shipmentOrder 出库单
     */
    private void updateInventory(ShipmentOrder shipmentOrder){
        List<ShipmentOrderDetail> detailList = shipmentOrder.getDetailList();
        for (ShipmentOrderDetail detail : detailList) {
            // 查询现有库存
            QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
            inventoryQueryWrapper.eq("warehouse_id",detail.getWarehouseId())
                    .eq("area_id",detail.getAreaId())
                    .eq("sku_id",detail.getSkuId());
            Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
            // 更新库存数量
            if (inventory != null){
                inventory.setQuantity(inventory.getQuantity().subtract(detail.getQuantity()));
                String username = SecurityUtil.getUserName();
                inventory.setUpdateBy(username);
                inventory.setUpdateTime(LocalDateTime.now());
                inventoryMapper.updateById(inventory);
            }else {
                throw new BusException(CodeEnum.WAREHOUSING_INSUFFICIENT);
            }
        }
    }

    /**
     * 更新库存详情
     * @param shipmentOrder 出库单
     */
    private void updateInventoryDetail(ShipmentOrder shipmentOrder){
        List<ShipmentOrderDetail> detailList = shipmentOrder.getDetailList();
        for (ShipmentOrderDetail detail : detailList) {
            // 获取库存详情
            InventoryDetail inventoryDetail = inventoryDetailMapper.selectById(detail.getInventoryDetailId());
            // 更新库存详情数量
            inventoryDetail.setRemainQuantity(inventoryDetail.getRemainQuantity().subtract(detail.getQuantity()));
            String username = SecurityUtil.getUserName();
            inventoryDetail.setUpdateBy(username);
            inventoryDetail.setUpdateTime(LocalDateTime.now());
            inventoryDetailMapper.updateById(inventoryDetail);
        }
    }

    /**
     * 保存库存历史记录
     * @param shipmentOrder 出库单
     */
    private void saveInventoryHistory(ShipmentOrder shipmentOrder){
        List<ShipmentOrderDetail> detailList = shipmentOrder.getDetailList();
        for (ShipmentOrderDetail detail : detailList) {
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(shipmentOrder.getId());
            inventoryHistory.setOrderType(2);
            inventoryHistory.setOrderNo(shipmentOrder.getShipmentOrderNo());
            inventoryHistory.setSkuId(detail.getSkuId());
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setAreaId(detail.getAreaId());
            inventoryHistory.setQuantity(detail.getQuantity().negate());
            inventoryHistory.setAmount(detail.getAmount().negate());
            inventoryHistory.setProductionDate(detail.getProductionDate());
            inventoryHistory.setExpirationDate(detail.getExpirationDate());

            String username = SecurityUtil.getUserName();
            inventoryHistory.setCreateBy(username);
            inventoryHistory.setUpdateBy(username);
            inventoryHistory.setCreateTime(LocalDateTime.now());
            inventoryHistory.setUpdateTime(LocalDateTime.now());

            inventoryHistoryMapper.insert(inventoryHistory);
        }
    }
}

