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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 盘库单服务层
 */
@Service
@Transactional
public class CheckOrderService {
    @Autowired
    private CheckOrderMapper checkOrderMapper;
    @Autowired
    private CheckOrderDetailMapper checkOrderDetailMapper;
    @Autowired
    private ReceiptOrderMapper receiptOrderMapper;
    @Autowired
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;
    @Autowired
    private ShipmentOrderMapper shipmentOrderMapper;
    @Autowired
    private ShipmentOrderDetailMapper shipmentOrderDetailMapper;

    @Autowired
    private OrderNumberUtil orderNumberUtil;
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;
    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;
    @Autowired
    private InventoryMapper inventoryMapper;


    /**
     * 查询盘库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param checkOrderNo 盘库单号，可选
     * @param checkOrderStatus 盘库单状态，可选（-1作废 0未盘库 1已盘库）
     * @return 盘库单分页列表
     */
    public IPage<CheckOrder> selectCheckOrderPage(Integer pageNo, Integer pageSize, String checkOrderNo, Integer checkOrderStatus){
        Page<CheckOrder> page = new Page<>(pageNo,pageSize);
        QueryWrapper<CheckOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(checkOrderNo)){
            queryWrapper.like("check_order_no",checkOrderNo);
        }
        if (checkOrderStatus != null){
            queryWrapper.eq("check_order_status",checkOrderStatus);
        }
        queryWrapper.orderByDesc("create_time");
        return checkOrderMapper.selectPage(page,queryWrapper);
    }

    /**
     * 根据ID查询盘库单
     * @param id 盘库单ID
     * @return 盘库单详细信息，包含盘库单详情列表
     */
    public CheckOrder getCheckOrderById(Long id){
        CheckOrder checkOrder = checkOrderMapper.selectById(id);
        if (checkOrder != null){
            QueryWrapper<CheckOrderDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("check_order_id",id);
            List<CheckOrderDetail> checkOrderDetails = checkOrderDetailMapper.selectList(queryWrapper);
            checkOrder.setDetailList(checkOrderDetails);
        }
        return checkOrder;
    }

    /**
     * 添加盘库单
     * @param checkOrder 盘库单信息
     * @return 盘库单号
     */
    public String addCheckOrder(CheckOrder checkOrder){
        String userName = SecurityUtil.getUserName();
        checkOrder.setCreateBy(userName);
        checkOrder.setUpdateBy(userName);
        checkOrder.setCreateTime(LocalDateTime.now());
        checkOrder.setUpdateTime(LocalDateTime.now());

        // 设置初始状态
        checkOrder.setCheckOrderStatus(0); // 未盘库
        // 生成盘库单号
        String checkOrderNo = "PK" + orderNumberUtil.generateOrderNumber();
        checkOrder.setCheckOrderNo(checkOrderNo);
        checkOrderMapper.insert(checkOrder);
        return checkOrderNo;
    }

    /**
     * 修改盘库单
     * @param checkOrder 盘库单信息
     * @return 修改结果
     */
    public boolean updateCheckOrder(CheckOrder checkOrder){
        if (checkOrder.getCheckOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        String userName = SecurityUtil.getUserName();
        checkOrder.setUpdateBy(userName);
        checkOrder.setUpdateTime(LocalDateTime.now());
        return checkOrderMapper.updateById(checkOrder) > 0;
    }

    /**
     * 删除盘库单
     * @param id 盘库单id
     * @return 操作结果
     */
    public boolean deleteCheckOrder(Long id){
        // 检查盘库单状态
        CheckOrder checkOrder = checkOrderMapper.selectById(id);
        if (checkOrder.getCheckOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }

        // 删除盘库单详情
        QueryWrapper<CheckOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_order_id",id);
        checkOrderDetailMapper.delete(queryWrapper);

        // 删除盘库单
        return checkOrderMapper.deleteById(id) > 0;
    }

    /**
     * 完成盘库
     * 盘库时，输入实际数量和账面数量进行比较。
     * 实际数量比账面数量多，称之为盘盈。
     * 实际数量比账面数量少，称之为盘亏。
     * 盘盈时需要将多出来的数量作为盘盈入库数量
     * 盘亏时需要将缺少的数量作为盘亏出库数量
     * @param id 盘库单ID
     * @return 是否完成盘库成功
     */
    public boolean completeCheckOrder(Long id){
        // 检查盘库单及盘库详情单
        CheckOrder checkOrder = this.getCheckOrderById(id);
        if (checkOrder == null){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_NOT_EXIST);
        }
        if (checkOrder.getCheckOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        List<CheckOrderDetail> detailList = checkOrder.getDetailList();
        if (detailList == null || detailList.isEmpty()){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_DETAIL_ISNULL);
        }

        // 拆分出 盘盈入库 和 盘亏出库 的盘库单详情
        List<CheckOrderDetail> profitList = new ArrayList<>(); // 盘盈列表
        List<CheckOrderDetail> lossList = new ArrayList<>(); // 盘亏列表
        for (CheckOrderDetail detail : detailList) {
            // 盘点数量（实际数量） - 库存数量（系统数量） = 变化数量
            BigDecimal diff = detail.getCheckQuantity().subtract(detail.getQuantity());
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                // 盘盈：变化数量 > 0;
                profitList.add(detail);
            } else if (diff.compareTo(BigDecimal.ZERO) < 0) {
                // 盘亏：变化数量 < 0;
                lossList.add(detail);
            }
            // 相等的不处理
        }

        // 生成盘盈入库单
        profitInBound(checkOrder, profitList);

        // 生成盘亏出库单
        lossOutBound(checkOrder, lossList);

        // 更新库存
        updateInventory(checkOrder);

        // 更新库存详情
        updateInventoryDetail(checkOrder);

        // 保存库存操作历史记录
        saveInventoryHistory(checkOrder);

        // 更新盘库单状态
        checkOrder.setCheckOrderStatus(1); // 已盘库
        checkOrder.setUpdateBy(SecurityUtil.getUserName());
        checkOrder.setUpdateTime(LocalDateTime.now());
        return checkOrderMapper.updateById(checkOrder) > 0;
    }

    /**
     * 盘盈入库操作，生成盘盈入库单
     * @param checkOrder 盘库单
     * @param profitList 盘盈详情列表
     */
    private void profitInBound(CheckOrder checkOrder, List<CheckOrderDetail> profitList){
        // 生成入库单
        ReceiptOrder receiptOrder = new ReceiptOrder();
        receiptOrder.setReceiptOrderNo("RK" + orderNumberUtil.generateOrderNumber());
        receiptOrder.setReceiptOrderType(2);
        receiptOrder.setReceiptOrderStatus(1); // 已入库
        receiptOrder.setWarehouseId(checkOrder.getWarehouseId());
        receiptOrder.setAreaId(checkOrder.getAreaId());
        receiptOrder.setRemark(checkOrder.getCheckOrderNo()); // 入库单备注盘库单号
        // 计算总入库数量
        BigDecimal quantitySum = BigDecimal.ZERO;
        for (CheckOrderDetail detail : profitList) {
            BigDecimal quantity = detail.getCheckQuantity().subtract(detail.getQuantity());
            quantitySum = quantitySum.add(quantity);
        }
        receiptOrder.setTotalQuantity(quantitySum);

        String userName = SecurityUtil.getUserName();
        receiptOrder.setCreateBy(userName);
        receiptOrder.setUpdateBy(userName);
        receiptOrder.setCreateTime(LocalDateTime.now());
        receiptOrder.setUpdateTime(LocalDateTime.now());
        receiptOrderMapper.insert(receiptOrder);

        // 生成入库单详情
        for (CheckOrderDetail detail : profitList) {
            ReceiptOrderDetail receiptOrderDetail = new ReceiptOrderDetail();
            receiptOrderDetail.setReceiptOrderId(receiptOrder.getId());
            receiptOrderDetail.setSkuId(detail.getSkuId());
            receiptOrderDetail.setWarehouseId(checkOrder.getWarehouseId());
            receiptOrderDetail.setAreaId(checkOrder.getAreaId());
            receiptOrderDetail.setProductionDate(detail.getProductionDate());
            receiptOrderDetail.setExpirationDate(detail.getExpirationDate());
            receiptOrderDetail.setQuantity(detail.getCheckQuantity().subtract(detail.getQuantity()));

            receiptOrderDetail.setCreateBy(userName);
            receiptOrderDetail.setUpdateBy(userName);
            receiptOrderDetail.setCreateTime(LocalDateTime.now());
            receiptOrderDetail.setUpdateTime(LocalDateTime.now());
            receiptOrderDetailMapper.insert(receiptOrderDetail);
        }
    }

    /**
     * 盘亏出库操作，生成盘亏出库单
     * @param checkOrder 盘库单信息
     * @param lossList 盘亏详情列表
     */
    private void lossOutBound(CheckOrder checkOrder,List<CheckOrderDetail> lossList){
        // 生成出库单
        ShipmentOrder shipmentOrder = new ShipmentOrder();
        shipmentOrder.setShipmentOrderNo("CK" + orderNumberUtil.generateOrderNumber());
        shipmentOrder.setShipmentOrderType(2);
        shipmentOrder.setShipmentOrderStatus(1);
        shipmentOrder.setWarehouseId(checkOrder.getWarehouseId());
        shipmentOrder.setAreaId(checkOrder.getAreaId());
        shipmentOrder.setRemark(checkOrder.getCheckOrderNo());
        // 计算总出库数量
        BigDecimal quantitySum = BigDecimal.ZERO;
        for (CheckOrderDetail detail : lossList) {
            BigDecimal quantity = detail.getCheckQuantity().subtract(detail.getQuantity()).negate();
            quantitySum = quantitySum.add(quantity);
        }
        shipmentOrder.setTotalQuantity(quantitySum);
        String userName = SecurityUtil.getUserName();
        shipmentOrder.setCreateBy(userName);
        shipmentOrder.setUpdateBy(userName);
        shipmentOrder.setCreateTime(LocalDateTime.now());
        shipmentOrder.setUpdateTime(LocalDateTime.now());
        shipmentOrderMapper.insert(shipmentOrder);

        // 生成出库单详情
        for (CheckOrderDetail detail : lossList) {
            ShipmentOrderDetail shipmentOrderDetail = new ShipmentOrderDetail();
            shipmentOrderDetail.setShipmentOrderId(shipmentOrder.getId());
            shipmentOrderDetail.setSkuId(detail.getSkuId());
            shipmentOrderDetail.setWarehouseId(checkOrder.getWarehouseId());
            shipmentOrderDetail.setAreaId(checkOrder.getAreaId());
            shipmentOrderDetail.setProductionDate(detail.getProductionDate());
            shipmentOrderDetail.setExpirationDate(detail.getExpirationDate());
            shipmentOrderDetail.setQuantity(detail.getCheckQuantity().subtract(detail.getQuantity()).negate());
            shipmentOrderDetail.setCreateBy(userName);
            shipmentOrderDetail.setUpdateBy(userName);
            shipmentOrderDetail.setCreateTime(LocalDateTime.now());
            shipmentOrderDetail.setUpdateTime(LocalDateTime.now());
            shipmentOrderDetailMapper.insert(shipmentOrderDetail);
        }
    }

    /**
     * 更新库存
     * @param checkOrder 盘库单信息
     */
    private void updateInventory(CheckOrder checkOrder){
        List<CheckOrderDetail> detailList = checkOrder.getDetailList();
        for (CheckOrderDetail detail : detailList) {
            // 查询现有库存
            QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_id",detail.getWarehouseId())
                    .eq("area_id",detail.getAreaId())
                    .eq("sku_id",detail.getSkuId());
            Inventory inventory = inventoryMapper.selectOne(queryWrapper);
            // 更新库存数量
            inventory.setQuantity(detail.getCheckQuantity());
            inventory.setUpdateTime(LocalDateTime.now());
            inventory.setUpdateBy(SecurityUtil.getUserName());
            inventoryMapper.updateById(inventory);
        }
    }

    /**
     * 更新库存详情
     * @param checkOrder 盘库单信息
     */
    private void updateInventoryDetail(CheckOrder checkOrder){
        List<CheckOrderDetail> detailList = checkOrder.getDetailList();
        for (CheckOrderDetail detail : detailList) {
            InventoryDetail inventoryDetail = inventoryDetailMapper.selectById(detail.getInventoryDetailId());
            inventoryDetail.setRemainQuantity(detail.getCheckQuantity());
            inventoryDetail.setUpdateTime(LocalDateTime.now());
            inventoryDetail.setUpdateBy(SecurityUtil.getUserName());
            inventoryDetailMapper.updateById(inventoryDetail);
        }
    }

    /**
     * 保存库存历史记录流水
     * @param checkOrder 盘库单信息
     */
    private void saveInventoryHistory(CheckOrder checkOrder){
        List<CheckOrderDetail> detailList = checkOrder.getDetailList();
        for (CheckOrderDetail detail : detailList) {
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderType(4); // 盘库
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setAreaId(detail.getAreaId());
            inventoryHistory.setSkuId(detail.getSkuId());
            // 盘点数量（实际数量） - 库存数量（系统数量） = 变化数量
            inventoryHistory.setQuantity(detail.getCheckQuantity().subtract(detail.getQuantity()));
            inventoryHistory.setProductionDate(detail.getProductionDate());
            inventoryHistory.setExpirationDate(detail.getExpirationDate());
            inventoryHistory.setOrderId(checkOrder.getId());
            inventoryHistory.setOrderNo(checkOrder.getCheckOrderNo());

            String userName = SecurityUtil.getUserName();
            inventoryHistory.setCreateBy(userName);
            inventoryHistory.setUpdateBy(userName);
            inventoryHistory.setCreateTime(LocalDateTime.now());
            inventoryHistory.setUpdateTime(LocalDateTime.now());
            inventoryHistoryMapper.insert(inventoryHistory);
        }
    }
}

