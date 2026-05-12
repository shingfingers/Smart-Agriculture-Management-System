package com.itbaizhan.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_warehousing.entity.InventoryDetail;
import com.itbaizhan.farm_warehousing.service.InventoryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 库存详情Controller
 */
@RestController
@RequestMapping("/inventoryDetail")
public class InventoryDetailController {
    @Autowired
    private InventoryDetailService inventoryDetailService;

    /**
     * 分页查询库存详情
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param warehouseId 仓库ID，可选
     * @param areaId 库区ID，可选
     * @param orderNo 入库单号，可选，模糊查询
     * @param itemName 商品名称，可选，模糊查询
     * @param itemCode 商品编号，可选，模糊查询
     * @param skuName 规格名称，可选，模糊查询
     * @param skuCode 规格编号，可选，模糊查询
     * @param receiptDate 入库日期，可选
     * @return 查询结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<InventoryDetail>> selectPage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            @RequestParam(value = "areaId", required = false) Long areaId,
            @RequestParam(value = "orderNo", required = false) String orderNo,
            @RequestParam(value = "itemName", required = false) String itemName,
            @RequestParam(value = "itemCode", required = false) String itemCode,
            @RequestParam(value = "skuName", required = false) String skuName,
            @RequestParam(value = "skuCode", required = false) String skuCode,
            @RequestParam(value = "receiptDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate receiptDate) {
        IPage<InventoryDetail> inventoryDetailPage = inventoryDetailService.selectPage(pageNum, pageSize, warehouseId, areaId, orderNo, itemName, itemCode, skuName, skuCode, receiptDate);
        return BaseResult.ok(inventoryDetailPage);
    }

    /**
     * 根据ID查询库存详情
     * @param id 库存详情ID
     * @return 查询结果
     */
    @GetMapping("/getInventoryDetailById")
    public BaseResult<InventoryDetail> getInventoryDetailById(@RequestParam("id") Long id) {
        InventoryDetail inventoryDetail = inventoryDetailService.getInventoryDetailById(id);
        return BaseResult.ok(inventoryDetail);
    }
}
