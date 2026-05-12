package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.InventoryHistory;
import com.itwork.farm_warehousing.service.InventoryHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存历史Controller
 */
@RestController
@RequestMapping("/inventoryHistory")
public class InventoryHistoryController {
    @Autowired
    private InventoryHistoryService inventoryHistoryService;

    /**
     * 分页查询库存历史列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param warehouseId 仓库ID，可选
     * @param areaId 库区ID，可选
     * @param itemName 商品名称，可选，模糊查询
     * @param itemCode 商品编号，可选，模糊查询
     * @param skuName 规格名称，可选，模糊查询
     * @param skuCode 规格编号，可选，模糊查询
     * @param orderType 操作类型（1.入库 2.出库 3.移库 4.盘库），可选
     * @param orderNo 操作单号，可选，模糊查询
     * @return 库存历史分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<InventoryHistory>> getInventoryHistoryList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            @RequestParam(value = "areaId", required = false) Long areaId,
            @RequestParam(value = "itemName", required = false) String itemName,
            @RequestParam(value = "itemCode", required = false) String itemCode,
            @RequestParam(value = "skuName", required = false) String skuName,
            @RequestParam(value = "skuCode", required = false) String skuCode,
            @RequestParam(value = "orderType", required = false) Integer orderType,
            @RequestParam(value = "orderNo", required = false) String orderNo) {
        IPage<InventoryHistory> inventoryHistoryPage = inventoryHistoryService.selectPage(pageNum, pageSize, warehouseId, areaId, itemName, itemCode, skuName, skuCode, orderType, orderNo);
        return BaseResult.ok(inventoryHistoryPage);
    }
}

