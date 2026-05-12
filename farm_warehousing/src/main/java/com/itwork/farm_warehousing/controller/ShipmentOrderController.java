package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.ShipmentOrder;
import com.itwork.farm_warehousing.service.ShipmentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 出库单控制层
 */
@RestController
@RequestMapping("/shipmentOrder")
public class ShipmentOrderController {
    @Autowired
    private ShipmentOrderService shipmentOrderService;

    /**
     * 查询出库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param shipmentOrderNo 出库单号，可选
     * @param shipmentOrderStatus 出库单状态，可选（0待出库 1已出库）
     * @return 出库单分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<ShipmentOrder>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)String shipmentOrderNo,
        @RequestParam(required = false)Integer shipmentOrderStatus){
        IPage<ShipmentOrder> shipmentOrderIPage = shipmentOrderService.selectShipmentOrderPage(pageNo, pageSize, shipmentOrderNo, shipmentOrderStatus);
        return BaseResult.ok(shipmentOrderIPage);
    }

    /**
     * 根据ID查询出库单
     * @param id 出库单ID
     * @return 出库单详细信息，包含出库单详情列表
     */
    @GetMapping("/getShipmentOrderById")
    public BaseResult<ShipmentOrder> getShipmentOrderById(@RequestParam Long id){
        ShipmentOrder shipmentOrder = shipmentOrderService.getShipmentOrderById(id);
        return BaseResult.ok(shipmentOrder);
    }

    /**
     * 添加出库单
     * @param shipmentOrder 出库单信息
     * @return 添加结果
     */
    @PostMapping("/addShipmentOrder")
    public BaseResult<String> addShipmentOrder(@RequestBody ShipmentOrder shipmentOrder){
        String shipmentOrderNo = shipmentOrderService.addShipmentOrder(shipmentOrder);
        return BaseResult.ok(shipmentOrderNo);
    }

    /**
     * 修改出库单
     * @param shipmentOrder 出库单信息
     * @return 修改结果
     */
    @PutMapping("/updateShipmentOrder")
    public BaseResult<?> updateShipmentOrder(@RequestBody ShipmentOrder shipmentOrder){
        shipmentOrderService.updateShipmentOrder(shipmentOrder);
        return BaseResult.ok();
    }

    /**
     * 删除出库单
     * @param id 出库单id
     * @return 操作结果
     */
    @DeleteMapping("/deleteShipmentOrder")
    public BaseResult<?> deleteShipmentOrder(@RequestParam Long id){
        shipmentOrderService.deleteShipmentOrder(id);
        return BaseResult.ok();
    }

    /**
     * 完成出库
     * @param id 出库单id
     * @return 操作结果
     */
    @PostMapping("/completeShipment")
    public BaseResult<?> completeShipment(@RequestParam Long id){
        shipmentOrderService.completeShipment(id);
        return BaseResult.ok();
    }
}

