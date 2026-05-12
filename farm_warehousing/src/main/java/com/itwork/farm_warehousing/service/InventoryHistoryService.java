package com.itwork.farm_warehousing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_warehousing.entity.InventoryHistory;
import com.itwork.farm_warehousing.mapper.InventoryHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存历史服务
 */
@Service
@Transactional
public class InventoryHistoryService {
    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;

    /**
     * 分页查询库存历史
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param warehouseId 仓库ID
     * @param areaId 库区ID
     * @param itemName 商品名称，模糊查询
     * @param itemCode 商品编号，模糊查询
     * @param skuName 规格名称，模糊查询
     * @param skuCode 规格编号，模糊查询
     * @param orderType 操作类型（1.入库 2.出库 3.移库 4.盘库）
     * @param orderNo 操作单号，模糊查询
     * @return 查询结果
     */
    public IPage<InventoryHistory> selectPage(Integer pageNo, Integer pageSize,
                                              Long warehouseId, Long areaId,
                                              String itemName, String itemCode,
                                              String skuName, String skuCode,
                                              Integer orderType, String orderNo){
        Page<InventoryHistory> page = new Page<>(pageNo, pageSize);
        return inventoryHistoryMapper.selectPageWithJoin(page, warehouseId, areaId, itemName, itemCode, skuName, skuCode, orderType, orderNo);
    }
}

