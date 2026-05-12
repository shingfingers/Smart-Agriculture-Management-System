package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.MovementOrder;
import com.itwork.farm_warehousing.service.MovementOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 移库单控制层
 */
@RestController
@RequestMapping("/movementOrder")
public class MovementOrderController {
    @Autowired
    private MovementOrderService movementOrderService;

    /**
     * 查询移库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param movementOrderNo 移库单号，可选
     * @param movementOrderStatus 移库单状态，可选（0待移库 1已移库）
     * @return 移库单分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<MovementOrder>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)String movementOrderNo,
        @RequestParam(required = false)Integer movementOrderStatus){
        IPage<MovementOrder> movementOrderIPage = movementOrderService.selectMovementOrderPage(pageNo, pageSize, movementOrderNo, movementOrderStatus);
        return BaseResult.ok(movementOrderIPage);
    }

    /**
     * 根据ID查询移库单
     * @param id 移库单ID
     * @return 移库单详细信息，包含移库单详情列表
     */
    @GetMapping("/getMovementOrderById")
    public BaseResult<MovementOrder> getMovementOrderById(@RequestParam Long id){
        MovementOrder movementOrder = movementOrderService.getMovementOrderById(id);
        return BaseResult.ok(movementOrder);
    }

    /**
     * 添加移库单
     * @param movementOrder 移库单信息
     * @return 添加结果
     */
    @PostMapping("/addMovementOrder")
    public BaseResult<String> addMovementOrder(@RequestBody MovementOrder movementOrder){
        String movementOrderNo = movementOrderService.addMovementOrder(movementOrder);
        return BaseResult.ok(movementOrderNo);
    }

    /**
     * 修改移库单
     * @param movementOrder 移库单信息
     * @return 修改结果
     */
    @PutMapping("/updateMovementOrder")
    public BaseResult<?> updateMovementOrder(@RequestBody MovementOrder movementOrder){
        movementOrderService.updateMovementOrder(movementOrder);
        return BaseResult.ok();
    }

    /**
     * 删除移库单
     * @param id 移库单id
     * @return 操作结果
     */
    @DeleteMapping("/deleteMovementOrder")
    public BaseResult<?> deleteMovementOrder(@RequestParam Long id){
        movementOrderService.deleteMovementOrder(id);
        return BaseResult.ok();
    }

    /**
     * 完成移库单
     * @param id 移库单id
     * @return 操作结果
     */
    @PostMapping("/completeMovement")
    public BaseResult<?> completeMovement(@RequestParam Long id){
        movementOrderService.completeMovement(id);
        return BaseResult.ok();
    }

}

