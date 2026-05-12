package com.itwork.farm_warehousing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_warehousing.entity.Inventory;
import com.itwork.farm_warehousing.mapper.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存服务
 */
@Service
@Transactional
public class InventoryService {
    @Autowired
    private InventoryMapper inventoryMapper;

    /**
     * 分页查询库存
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param warehouseId 仓库ID
     * @param areaId 库区ID
     * @param itemName 商品名称，模糊查询
     * @param itemCode 商品编号，模糊查询
     * @param skuName 规格名称，模糊查询
     * @param skuCode 规格编号，模糊查询
     * @return 查询结果
     */
    public IPage<Inventory> selectPage(Integer pageNo, Integer pageSize,
                                      Long warehouseId, Long areaId,
                                      String itemName, String itemCode,
                                      String skuName, String skuCode){
        Page<Inventory> page = new Page<>(pageNo, pageSize);
        return inventoryMapper.selectPageWithJoin(page, warehouseId, areaId, itemName, itemCode, skuName, skuCode);
    }
}

