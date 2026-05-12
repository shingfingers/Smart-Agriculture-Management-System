package com.itwork.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.OrderNumberUtil;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_warehousing.entity.*;
import com.itwork.farm_warehousing.mapper.*;
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
 * 移库单服务层
 */
@Service
@Transactional
public class MovementOrderService {
    @Autowired
    private MovementOrderMapper movementOrderMapper;
    @Autowired
    private MovementOrderDetailMapper movementOrderDetailMapper;
    @Autowired
    private OrderNumberUtil orderNumberUtil;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;
    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;

    /**
     * 查询移库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param movementOrderNo 移库单号，可选
     * @param movementOrderStatus 移库单状态，可选（0待移库 1已移库）
     * @return 移库单分页列表
     */
    public IPage<MovementOrder> selectMovementOrderPage(Integer pageNo, Integer pageSize, String movementOrderNo, Integer movementOrderStatus){
        Page<MovementOrder> page = new Page<>(pageNo,pageSize);
        QueryWrapper<MovementOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(movementOrderNo)){
            queryWrapper.like("movement_order_no",movementOrderNo);
        }
        if (movementOrderStatus != null){
            queryWrapper.eq("movement_order_status",movementOrderStatus);
        }
        queryWrapper.orderByDesc("create_time");
        return movementOrderMapper.selectPage(page,queryWrapper);
    }

    /**
     * 根据ID查询移库单
     * @param id 移库单ID
     * @return 移库单详细信息，包含移库单详情列表
     */
    public MovementOrder getMovementOrderById(Long id){
        MovementOrder movementOrder = movementOrderMapper.selectById(id);
        if (movementOrder != null){
            QueryWrapper<MovementOrderDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("movement_order_id",id);
            List<MovementOrderDetail> movementOrderDetails = movementOrderDetailMapper.selectList(queryWrapper);
            movementOrder.setDetailList(movementOrderDetails);
        }
        return movementOrder;
    }

    /**
     * 添加移库单
     * @param movementOrder 移库单信息
     * @return 移库单号
     */
    public String addMovementOrder(MovementOrder movementOrder){
        String userName = SecurityUtil.getUserName();
        movementOrder.setCreateBy(userName);
        movementOrder.setUpdateBy(userName);
        movementOrder.setCreateTime(LocalDateTime.now());
        movementOrder.setUpdateTime(LocalDateTime.now());

        // 设置初始状态
        movementOrder.setMovementOrderStatus(0); // 待移库
        // 生成移库单号
        String movementOrderNo = "YK" + orderNumberUtil.generateOrderNumber();
        movementOrder.setMovementOrderNo(movementOrderNo);
        movementOrderMapper.insert(movementOrder);
        return movementOrderNo;
    }

    /**
     * 修改移库单
     * @param movementOrder 移库单信息
     * @return 修改结果
     */
    public boolean updateMovementOrder(MovementOrder movementOrder){
        if (movementOrder.getMovementOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        String userName = SecurityUtil.getUserName();
        movementOrder.setUpdateBy(userName);
        movementOrder.setUpdateTime(LocalDateTime.now());
        return movementOrderMapper.updateById(movementOrder) > 0;
    }

    /**
     * 删除移库单
     * @param id 移库单id
     * @return 操作结果
     */
    public boolean deleteMovementOrder(Long id){
        // 检查移库单状态
        MovementOrder movementOrder = movementOrderMapper.selectById(id);
        if (movementOrder.getMovementOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        // 删除移库单详情
        QueryWrapper<MovementOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movement_order_id",id);
        movementOrderDetailMapper.delete(queryWrapper);
        // 删除移库单
        return movementOrderMapper.deleteById(id) > 0;
    }

    /**
     * 完成移库
     * 更新库存、库存详情、库存历史记录
     * @param id 移库单id
     * @return 操作结果
     */
    public boolean completeMovement(Long id){
        // 检查移库单状态
        MovementOrder movementOrder = this.getMovementOrderById(id);
        if (movementOrder == null){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_NOT_EXIST);
        }
        if (movementOrder.getMovementOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        // 合并移库单详情
        mergeMovementOrderDetail(movementOrder);
        // 验证移库单详情
        validateMovementOrderDetail(movementOrder);
        // 修改库存
        updateInventory(movementOrder);
        // 修改库存详情
        updateInventoryDetail(movementOrder);
        // 添加库存历史记录
        saveInventoryHistory(movementOrder);
        // 更新移库单状态
        movementOrder.setMovementOrderStatus(1); // 已移库
        movementOrder.setUpdateBy(SecurityUtil.getUserName());
        movementOrder.setUpdateTime(LocalDateTime.now());

        return movementOrderMapper.updateById(movementOrder) > 0;
    }

    /**
     * 合并移库单详情 按照源仓库+库区+目标仓库+库区+SKU合并
     * @param movementOrder 移库单信息
     */
    private void mergeMovementOrderDetail(MovementOrder movementOrder){
        // 创建一个Map，key为源仓库+库区+目标仓库+库区+SKU，value为入库单详情
        Map<String, MovementOrderDetail> detailMap = new HashMap<>();

        List<MovementOrderDetail> detailList = movementOrder.getDetailList();
        for (MovementOrderDetail movementOrderDetail : detailList) {
            String key = movementOrderDetail.getSourceWarehouseId() + "_" + movementOrderDetail.getSourceAreaId() +
                    "_" + movementOrderDetail.getTargetWarehouseId() + "_" + movementOrderDetail.getTargetAreaId() +
                    "_" + movementOrderDetail.getSkuId();
            if (detailMap.containsKey(key)){
                // 如果存在相同物料，则合并数量
                MovementOrderDetail existDetail = detailMap.get(key);
                existDetail.setQuantity(existDetail.getQuantity().add(movementOrderDetail.getQuantity()));
            }else {
                // 如果不存在相同物料，则创建新的物料
                detailMap.put(key,movementOrderDetail);
            }
        }
        movementOrder.setDetailList(new ArrayList<>(detailMap.values()));
    }

    /**
     * 检查移库单详情，检查源仓库库存是否充足
     * @param movementOrder 移库单信息
     */
    private void validateMovementOrderDetail(MovementOrder movementOrder){
        // 是否有详情
        List<MovementOrderDetail> detailList = movementOrder.getDetailList();
        if (detailList == null || detailList.isEmpty()){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_DETAIL_ISNULL);
        }

        // 遍历详情，检查库存是否充足
        for (MovementOrderDetail orderDetail : detailList) {
            // 检查库存是否充足
            QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
            inventoryQueryWrapper.eq("warehouse_id",orderDetail.getSourceWarehouseId())
                    .eq("area_id",orderDetail.getSourceAreaId())
                    .eq("sku_id",orderDetail.getSkuId());
            Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
            if (inventory == null || inventory.getQuantity().compareTo(orderDetail.getQuantity()) < 0){
                throw new BusException(CodeEnum.WAREHOUSING_INSUFFICIENT);
            }

            // 检查库存详情是否充足
            InventoryDetail inventoryDetail = inventoryDetailMapper.selectById(orderDetail.getInventoryDetailId());
            if (inventoryDetail == null || inventoryDetail.getQuantity().compareTo(orderDetail.getQuantity()) < 0){
                throw new BusException(CodeEnum.WAREHOUSING_INSUFFICIENT);
            }
        }
    }

    /**
     * 更新库存数量，减少源仓库库存，新建目标仓库库存
     * @param movementOrder 移库单信息
     */
    private void updateInventory(MovementOrder movementOrder){
        List<MovementOrderDetail> detailList = movementOrder.getDetailList();
        for (MovementOrderDetail detail : detailList) {
            // 1.减少源仓库库存
            QueryWrapper<Inventory> sourceQueryWrapper = new QueryWrapper<>();
            sourceQueryWrapper.eq("warehouse_id",detail.getSourceWarehouseId())
                    .eq("area_id",detail.getSourceAreaId())
                    .eq("sku_id",detail.getSkuId());
            Inventory sourceInventory = inventoryMapper.selectOne(sourceQueryWrapper);
            if (sourceInventory != null){
                sourceInventory.setQuantity(sourceInventory.getQuantity().subtract(detail.getQuantity()));
                String username = SecurityUtil.getUserName();
                sourceInventory.setUpdateBy(username);
                sourceInventory.setUpdateTime(LocalDateTime.now());
                inventoryMapper.updateById(sourceInventory);
            }else {
                throw new BusException(CodeEnum.WAREHOUSING_INSUFFICIENT);
            }

            // 2.增加目标仓库库存
            QueryWrapper<Inventory> targetQueryWrapper = new QueryWrapper<>();
            targetQueryWrapper.eq("warehouse_id",detail.getTargetWarehouseId())
                    .eq("area_id",detail.getTargetAreaId())
                    .eq("sku_id",detail.getSkuId());
            Inventory targetInventory = inventoryMapper.selectOne(targetQueryWrapper);
            if (targetInventory == null){
                // 新增库存
                targetInventory = new Inventory();
                targetInventory.setWarehouseId(detail.getTargetWarehouseId());
                targetInventory.setAreaId(detail.getTargetAreaId());
                targetInventory.setSkuId(detail.getSkuId());
                targetInventory.setQuantity(detail.getQuantity());

                String username = SecurityUtil.getUserName();
                targetInventory.setCreateBy(username);
                targetInventory.setUpdateBy(username);
                targetInventory.setCreateTime(LocalDateTime.now());
                targetInventory.setUpdateTime(LocalDateTime.now());
                inventoryMapper.insert(targetInventory);
            }else {
                // 更新库存数量
                targetInventory.setQuantity(targetInventory.getQuantity().add(detail.getQuantity()));

                String username = SecurityUtil.getUserName();
                targetInventory.setUpdateBy(username);
                targetInventory.setUpdateTime(LocalDateTime.now());
                inventoryMapper.updateById(targetInventory);
            }
        }
    }

    /**
     * 更新库存详情数量，减少源仓库库存详情数量，增加目标仓库库存详情数量
     * @param movementOrder 移库单信息
     */
    private void updateInventoryDetail(MovementOrder movementOrder){
        List<MovementOrderDetail> detailList = movementOrder.getDetailList();
        for (MovementOrderDetail detail : detailList) {
            // 1.减少源仓库库存详情数量
            InventoryDetail inventoryDetail = inventoryDetailMapper.selectById(detail.getInventoryDetailId());
            inventoryDetail.setRemainQuantity(inventoryDetail.getRemainQuantity().subtract(detail.getQuantity()));
            String username = SecurityUtil.getUserName();
            inventoryDetail.setUpdateBy(username);
            inventoryDetail.setUpdateTime(LocalDateTime.now());
            inventoryDetailMapper.updateById(inventoryDetail);
            // 2.增加目标仓库库存详情数量
            // 如果目标仓库存在相同入库单号的库存详情，则累加数量，否则新增一条库存详情
            QueryWrapper<InventoryDetail> targetQueryWrapper = new QueryWrapper<>();
            targetQueryWrapper.eq("warehouse_id",detail.getTargetWarehouseId())
                    .eq("area_id",detail.getTargetAreaId())
                    .eq("sku_id",detail.getSkuId())
                    .eq("order_no",inventoryDetail.getOrderNo());
            InventoryDetail inventoryDetailTarget = inventoryDetailMapper.selectOne(targetQueryWrapper);
            if (inventoryDetailTarget == null){
                inventoryDetailTarget = new InventoryDetail();
                inventoryDetailTarget.setWarehouseId(detail.getTargetWarehouseId());
                inventoryDetailTarget.setAreaId(detail.getTargetAreaId());
                inventoryDetailTarget.setSkuId(detail.getSkuId());
                inventoryDetailTarget.setQuantity(detail.getQuantity());
                inventoryDetailTarget.setRemainQuantity(detail.getQuantity());
                inventoryDetailTarget.setExpirationDate(inventoryDetail.getExpirationDate());
                inventoryDetailTarget.setProductionDate(inventoryDetail.getProductionDate());

                // 新增的库存详情，入库单ID，入库单单号，金额和之前的库存详情一直
                inventoryDetailTarget.setReceiptOrderId(inventoryDetail.getReceiptOrderId());
                inventoryDetailTarget.setOrderNo(inventoryDetail.getOrderNo());
                inventoryDetailTarget.setAmount(inventoryDetail.getAmount());

                inventoryDetailTarget.setCreateBy(username);
                inventoryDetailTarget.setUpdateBy(username);
                inventoryDetailTarget.setCreateTime(LocalDateTime.now());
                inventoryDetailTarget.setUpdateTime(LocalDateTime.now());
                inventoryDetailMapper.insert(inventoryDetailTarget);
            }else {
                // 累加数量
                inventoryDetailTarget.setQuantity(inventoryDetailTarget.getQuantity().add(detail.getQuantity()));
                inventoryDetailTarget.setRemainQuantity(inventoryDetailTarget.getRemainQuantity().add(detail.getQuantity()));

                inventoryDetailTarget.setUpdateBy(username);
                inventoryDetailTarget.setUpdateTime(LocalDateTime.now());
                inventoryDetailMapper.updateById(inventoryDetailTarget);
            }
        }
    }

    /**
     * 保存库存历史记录
     * @param movementOrder 移库单信息
     */
    private void saveInventoryHistory(MovementOrder movementOrder){
        List<MovementOrderDetail> detailList = movementOrder.getDetailList();
        for (MovementOrderDetail detail : detailList) {
            // 移库时的原始库存详情
            InventoryDetail inventoryDetail = inventoryDetailMapper.selectById(detail.getInventoryDetailId());

            // 源仓库的出库流水
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setWarehouseId(detail.getSourceWarehouseId());
            inventoryHistory.setAreaId(detail.getSourceAreaId());
            inventoryHistory.setSkuId(detail.getSkuId());
            inventoryHistory.setQuantity(detail.getQuantity().negate()); // 负数表示出库
            inventoryHistory.setProductionDate(inventoryDetail.getProductionDate());
            inventoryHistory.setExpirationDate(inventoryDetail.getExpirationDate());
            inventoryHistory.setAmount(inventoryDetail.getAmount());
            inventoryHistory.setOrderId(movementOrder.getId());
            inventoryHistory.setOrderNo(movementOrder.getMovementOrderNo());
            inventoryHistory.setOrderType(3); // 移库单
            inventoryHistory.setRemark(movementOrder.getRemark());

            String username = SecurityUtil.getUserName();
            inventoryHistory.setCreateBy(username);
            inventoryHistory.setUpdateBy(username);
            inventoryHistory.setCreateTime(LocalDateTime.now());
            inventoryHistory.setUpdateTime(LocalDateTime.now());

            // 目标仓库的入库流水
            InventoryHistory inventoryHistoryTarget = new InventoryHistory();
            inventoryHistoryTarget.setWarehouseId(detail.getTargetWarehouseId());
            inventoryHistoryTarget.setAreaId(detail.getTargetAreaId());
            inventoryHistoryTarget.setSkuId(detail.getSkuId());
            inventoryHistoryTarget.setQuantity(detail.getQuantity()); // 正数
            inventoryHistoryTarget.setProductionDate(inventoryDetail.getProductionDate());
            inventoryHistoryTarget.setExpirationDate(inventoryDetail.getExpirationDate());
            inventoryHistoryTarget.setAmount(inventoryDetail.getAmount());
            inventoryHistoryTarget.setOrderId(movementOrder.getId());
            inventoryHistoryTarget.setOrderNo(movementOrder.getMovementOrderNo());
            inventoryHistoryTarget.setOrderType(3); // 移库
            inventoryHistoryTarget.setRemark(movementOrder.getRemark());

            inventoryHistoryTarget.setCreateBy(username);
            inventoryHistoryTarget.setUpdateBy(username);
            inventoryHistoryTarget.setCreateTime(LocalDateTime.now());
            inventoryHistoryTarget.setUpdateTime(LocalDateTime.now());

            inventoryHistoryMapper.insert(inventoryHistory);
        }
    }

}

