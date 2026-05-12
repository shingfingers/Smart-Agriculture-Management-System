package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.ShipmentOrderDetail;
import com.itwork.farm_warehousing.service.ShipmentOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 出库单详情控制层
 */
@RestController
@RequestMapping("/shipmentOrderDetail")
public class ShipmentOrderDetailController {
    @Autowired
    private ShipmentOrderDetailService shipmentOrderDetailService;

    /**
     * 查询出库单详情分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param shipmentOrderId 出库单id，可选
     * @return 出库单详情分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<ShipmentOrderDetail>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)Long shipmentOrderId){
        IPage<ShipmentOrderDetail> shipmentOrderDetailIPage = shipmentOrderDetailService.selectShipmentOrderDetailPage(pageNo, pageSize, shipmentOrderId);
        return BaseResult.ok(shipmentOrderDetailIPage);
    }

    /**
     * 根据ID查询出库单详情
     * @param id 出库单详情ID
     * @return 出库单详情
     */
    @GetMapping("/getShipmentOrderDetailById")
    public BaseResult<ShipmentOrderDetail> getShipmentOrderDetailById(@RequestParam Long id){
        ShipmentOrderDetail shipmentOrderDetail = shipmentOrderDetailService.getShipmentOrderDetailById(id);
        return BaseResult.ok(shipmentOrderDetail);
    }

    /**
     * 添加出库单详情
     * @param shipmentOrderDetail 出库单详情
     * @return 添加结果
     */
    @PostMapping("/addShipmentOrderDetail")
    public BaseResult<?> addShipmentOrderDetail(@RequestBody ShipmentOrderDetail shipmentOrderDetail){
        shipmentOrderDetailService.addShipmentOrderDetail(shipmentOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 修改出库单详情
     * @param shipmentOrderDetail 出库单详情
     * @return 修改结果
     */
    @PutMapping("/updateShipmentOrderDetail")
    public BaseResult<?> updateShipmentOrderDetail(@RequestBody ShipmentOrderDetail shipmentOrderDetail){
        shipmentOrderDetailService.updateShipmentOrderDetail(shipmentOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 删除出库单详情
     * @param ids 出库单详情id，多个id用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteShipmentOrderDetail")
    public BaseResult<?> deleteShipmentOrderDetail(@RequestParam String ids){
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        shipmentOrderDetailService.deleteShipmentOrderDetail(idList);
        return BaseResult.ok();
    }
}

