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
 * 入库单服务层
 */
@Service
@Transactional
public class ReceiptOrderService {
    @Autowired
    private ReceiptOrderMapper receiptOrderMapper;
    @Autowired
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;
    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;
    @Autowired
    private OrderNumberUtil orderNumberUtil;

    /**
     * 查询入库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param receiptOrderNo 入库单号，可选
     * @param receiptOrderStatus 入库单状态，可选（0待入库 1已入库）
     * @return
     */
    public IPage<ReceiptOrder>  selectReceiptOrderPage(Integer pageNo, Integer pageSize, String receiptOrderNo, Integer receiptOrderStatus){
        Page<ReceiptOrder> page = new Page<>(pageNo,pageSize);
        QueryWrapper<ReceiptOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(receiptOrderNo)){
            queryWrapper.like("receipt_order_no",receiptOrderNo);
        }
        if (receiptOrderStatus != null){
            queryWrapper.eq("receipt_order_status",receiptOrderStatus);
        }
        queryWrapper.orderByDesc("create_time");
        return receiptOrderMapper.selectPage(page,queryWrapper);
    }

    /**
     * 根据ID查询入库单
     * @param id 入库单ID
     * @return 入库单详细信息，包含入库单详情列表
     */
    public ReceiptOrder getReceiptOrderById(Long id){
        ReceiptOrder receiptOrder = receiptOrderMapper.selectById(id);
        if (receiptOrder != null){
            QueryWrapper<ReceiptOrderDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("receipt_order_id",id);
            List<ReceiptOrderDetail> receiptOrderDetails = receiptOrderDetailMapper.selectList(queryWrapper);
            receiptOrder.setDetailList(receiptOrderDetails);
        }
        return receiptOrder;
    }

    /**
     * 添加入库单
     * @param receiptOrder 入库单信息
     * @return 入库单号
     */
    public String addReceiptOrder(ReceiptOrder receiptOrder){
        String userName = SecurityUtil.getUserName();
        receiptOrder.setCreateBy(userName);
        receiptOrder.setUpdateBy(userName);
        receiptOrder.setCreateTime(LocalDateTime.now());
        receiptOrder.setUpdateTime(LocalDateTime.now());

        // 设置初始状态
        receiptOrder.setReceiptOrderStatus(0); // 待入库
        // 生成入库单号
        String receiptOrderNo = "RK" + orderNumberUtil.generateOrderNumber();
        receiptOrder.setReceiptOrderNo(receiptOrderNo);
        // 设置入库类型
        receiptOrder.setReceiptOrderType(1);
        receiptOrderMapper.insert(receiptOrder);
        return receiptOrderNo;
    }

    /**
     * 修改入库单
     * @param receiptOrder 入库单信息
     * @return 修改结果
     */
    public boolean updateReceiptOrder(ReceiptOrder receiptOrder){
        if (receiptOrder.getReceiptOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        String userName = SecurityUtil.getUserName();
        receiptOrder.setUpdateBy(userName);
        receiptOrder.setUpdateTime(LocalDateTime.now());
        return receiptOrderMapper.updateById(receiptOrder) > 0;
    }

    /**
     * 删除入库单
     * @param id 入库单id
     * @return 操作结果
     */
    public boolean deleteReceiptOrder(Long id){
        // 检查入库单状态
        ReceiptOrder receiptOrder = receiptOrderMapper.selectById(id);
        if (receiptOrder.getReceiptOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        // 删除入库单详情
        QueryWrapper<ReceiptOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receipt_order_id",id);
        receiptOrderDetailMapper.delete(queryWrapper);
        // 删除入库单
        return receiptOrderMapper.deleteById(id) > 0;
    }

    /**
     * 完成入库操作
     * 更新库存，生成库存明细和库存历史记录
     * @param id 入库单id
     * @return 操作结果
     */
    public boolean completeReceipt(Long id){
        // 验证入库单以及入库单详情
        ReceiptOrder receiptOrder = getReceiptOrderById(id);
        if (receiptOrder == null){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_NOT_EXIST);
        }
        if (receiptOrder.getReceiptOrderStatus() == 1){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_ALL_READY);
        }
        List<ReceiptOrderDetail> detailList = receiptOrder.getDetailList();
        if (detailList == null || detailList.size() == 0 || detailList.isEmpty() ){
            throw new BusException(CodeEnum.WAREHOUSING_ORDER_DETAIL_ISNULL);
        }

        // 合并入库单详情中的数量
        mergeReceiptOrderDetail(receiptOrder);
        // 更新库存
        updateInventory(receiptOrder);
        // 保存库存明细
        saveInventoryDetail(receiptOrder);
        // 保存库存历史记录
        saveInventoryHistory(receiptOrder);
        // 更新入库单状态
        receiptOrder.setReceiptOrderStatus(1); // 已入库
        receiptOrder.setUpdateBy(SecurityUtil.getUserName());
        receiptOrder.setUpdateTime(LocalDateTime.now());
        return receiptOrderMapper.updateById(receiptOrder) > 0;

    }

    /**
     * 合并入库单详情，按仓库+库区+SKU分组合并数量
     * @param receiptOrder 入库单
     */
    private void mergeReceiptOrderDetail(ReceiptOrder receiptOrder){
        // 创建一个Map，key为仓库+库区+SKU，value为入库单详情
        Map<String,ReceiptOrderDetail> detailMap = new HashMap<>();

        List<ReceiptOrderDetail> detailList = receiptOrder.getDetailList();
        for (ReceiptOrderDetail orderDetail : detailList) {
            String key = orderDetail.getWarehouseId() + "_" + orderDetail.getAreaId() + "_" + orderDetail.getSkuId();
            if (detailMap.containsKey(key)){
                // 如果已存在相同物料，则累加数量
                ReceiptOrderDetail existDetail = detailMap.get(key);
                existDetail.setQuantity(existDetail.getQuantity().add(orderDetail.getQuantity()));
            }else {
                // 如果不存在相同物料，则创建新的物料
                detailMap.put(key,orderDetail);
            }
        }
        receiptOrder.setDetailList(new ArrayList<>(detailMap.values()));
    }

    /**
     * 更新库存数量
     * 如果存在相同物料，则累加数量
     * 如果不存在相同物料，则创建新的物料
     * @param receiptOrder
     */
    private void updateInventory(ReceiptOrder receiptOrder){
        List<ReceiptOrderDetail> detailList = receiptOrder.getDetailList();
        for (ReceiptOrderDetail detail : detailList) {
            // 查询现有库存
            QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse_id",detail.getWarehouseId());
            queryWrapper.eq("area_id",detail.getAreaId());
            queryWrapper.eq("sku_id",detail.getSkuId());
            Inventory inventory = inventoryMapper.selectOne(queryWrapper);
            if (inventory == null){
                // 新增库存
                inventory = new Inventory();
                inventory.setWarehouseId(detail.getWarehouseId());
                inventory.setAreaId(detail.getAreaId());
                inventory.setSkuId(detail.getSkuId());
                inventory.setQuantity(detail.getQuantity());
                String userName = SecurityUtil.getUserName();
                inventory.setCreateBy(userName);
                inventory.setUpdateBy(userName);
                inventory.setCreateTime(LocalDateTime.now());
                inventory.setUpdateTime(LocalDateTime.now());
                inventoryMapper.insert(inventory);
            }else {
                // 更新库存数量
                inventory.setQuantity(inventory.getQuantity().add(detail.getQuantity()));
                String userName = SecurityUtil.getUserName();
                inventory.setUpdateBy(userName);
                inventory.setUpdateTime(LocalDateTime.now());
                inventoryMapper.updateById(inventory);
            }
        }
    }


    /**
     * 保存库存明细
     * @param receiptOrder 入库单
     */
    private void saveInventoryDetail(ReceiptOrder receiptOrder){
        List<ReceiptOrderDetail> detailList = receiptOrder.getDetailList();
        for (ReceiptOrderDetail orderDetail : detailList) {
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setReceiptOrderId(receiptOrder.getId());
            inventoryDetail.setOrderNo(receiptOrder.getReceiptOrderNo());
            inventoryDetail.setSkuId(orderDetail.getSkuId());
            inventoryDetail.setWarehouseId(orderDetail.getWarehouseId());
            inventoryDetail.setAreaId(orderDetail.getAreaId());
            inventoryDetail.setQuantity(orderDetail.getQuantity());
            inventoryDetail.setAmount(orderDetail.getAmount());
            inventoryDetail.setProductionDate(orderDetail.getProductionDate());
            inventoryDetail.setExpirationDate(orderDetail.getExpirationDate());
            inventoryDetail.setRemainQuantity(orderDetail.getQuantity());

            String userName = SecurityUtil.getUserName();
            inventoryDetail.setCreateBy(userName);
            inventoryDetail.setUpdateBy(userName);
            inventoryDetail.setCreateTime(LocalDateTime.now());
            inventoryDetail.setUpdateTime(LocalDateTime.now());

            inventoryDetailMapper.insert(inventoryDetail);
        }
    }

    /**
     * 保存库存历史记录
     * 为每个入库单详情创建对应的库存历史记录
     * @param receiptOrder 入库单
     */
    private void saveInventoryHistory(ReceiptOrder receiptOrder){
        List<ReceiptOrderDetail> detailList = receiptOrder.getDetailList();
        for (ReceiptOrderDetail detail : detailList) {
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(receiptOrder.getId());
            inventoryHistory.setOrderType(1); // 入库操作
            inventoryHistory.setOrderNo(receiptOrder.getReceiptOrderNo());
            inventoryHistory.setSkuId(detail.getSkuId());
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setAreaId(detail.getAreaId());
            inventoryHistory.setQuantity(detail.getQuantity());
            inventoryHistory.setAmount(detail.getAmount());
            inventoryHistory.setProductionDate(detail.getProductionDate());
            inventoryHistory.setExpirationDate(detail.getExpirationDate());

            String userName = SecurityUtil.getUserName();
            inventoryHistory.setCreateBy(userName);
            inventoryHistory.setUpdateBy(userName);
            inventoryHistory.setCreateTime(LocalDateTime.now());
            inventoryHistory.setUpdateTime(LocalDateTime.now());

            inventoryHistoryMapper.insert(inventoryHistory);
        }
    }
}
