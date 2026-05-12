package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.CheckOrder;
import com.itwork.farm_warehousing.service.CheckOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 盘库单控制层
 */
@RestController
@RequestMapping("/checkOrder")
public class CheckOrderController {
    @Autowired
    private CheckOrderService checkOrderService;

    /**
     * 查询盘库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param checkOrderNo 盘库单号，可选
     * @param checkOrderStatus 盘库单状态，可选（-1作废 0未盘库 1已盘库）
     * @return 盘库单分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<CheckOrder>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)String checkOrderNo,
        @RequestParam(required = false)Integer checkOrderStatus){
        IPage<CheckOrder> checkOrderIPage = checkOrderService.selectCheckOrderPage(pageNo, pageSize, checkOrderNo, checkOrderStatus);
        return BaseResult.ok(checkOrderIPage);
    }

    /**
     * 根据ID查询盘库单
     * @param id 盘库单ID
     * @return 盘库单详细信息，包含盘库单详情列表
     */
    @GetMapping("/getCheckOrderById")
    public BaseResult<CheckOrder> getCheckOrderById(@RequestParam Long id){
        CheckOrder checkOrder = checkOrderService.getCheckOrderById(id);
        return BaseResult.ok(checkOrder);
    }

    /**
     * 添加盘库单
     * @param checkOrder 盘库单信息
     * @return 添加结果
     */
    @PostMapping("/addCheckOrder")
    public BaseResult<String> addCheckOrder(@RequestBody CheckOrder checkOrder){
        String checkOrderNo = checkOrderService.addCheckOrder(checkOrder);
        return BaseResult.ok(checkOrderNo);
    }

    /**
     * 修改盘库单
     * @param checkOrder 盘库单信息
     * @return 修改结果
     */
    @PutMapping("/updateCheckOrder")
    public BaseResult<?> updateCheckOrder(@RequestBody CheckOrder checkOrder){
        checkOrderService.updateCheckOrder(checkOrder);
        return BaseResult.ok();
    }

    /**
     * 删除盘库单
     * @param id 盘库单id
     * @return 操作结果
     */
    @DeleteMapping("/deleteCheckOrder")
    public BaseResult<?> deleteCheckOrder(@RequestParam Long id){
        checkOrderService.deleteCheckOrder(id);
        return BaseResult.ok();
    }

    /**
     * 完成盘库
     * @param id 盘库单id
     * @return 操作结果
     */
    @PostMapping("/completeCheck")
    public BaseResult<?> completeCheck(@RequestParam Long id){
        checkOrderService.completeCheckOrder(id);
        return BaseResult.ok();
    }

}

