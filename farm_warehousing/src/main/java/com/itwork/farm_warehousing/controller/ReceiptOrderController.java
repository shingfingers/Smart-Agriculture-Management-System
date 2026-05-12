package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.ReceiptOrder;
import com.itwork.farm_warehousing.service.ReceiptOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 入库单控制层
 */
@RestController
@RequestMapping("/receiptOrder")
public class ReceiptOrderController {
    @Autowired
    private ReceiptOrderService receiptOrderService;

    /**
     * 查询入库单分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param receiptOrderNo 入库单号，可选
     * @param receiptOrderStatus 入库单状态，可选（0待入库 1已入库）
     * @return 入库单分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<ReceiptOrder>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)String receiptOrderNo,
        @RequestParam(required = false)Integer receiptOrderStatus){
        IPage<ReceiptOrder> receiptOrderIPage = receiptOrderService.selectReceiptOrderPage(pageNo, pageSize, receiptOrderNo, receiptOrderStatus);
        return BaseResult.ok(receiptOrderIPage);
    }

    /**
     * 根据ID查询入库单
     * @param id 入库单ID
     * @return 入库单详细信息，包含入库单详情列表
     */
    @GetMapping("/getReceiptOrderById")
    public BaseResult<ReceiptOrder> getReceiptOrderById(@RequestParam Long id){
        ReceiptOrder receiptOrder = receiptOrderService.getReceiptOrderById(id);
        return BaseResult.ok(receiptOrder);
    }

    /**
     * 添加入库单
     * @param receiptOrder 入库单信息
     * @return 添加结果
     */
    @PostMapping("/addReceiptOrder")
    public BaseResult<String> addReceiptOrder(@RequestBody ReceiptOrder receiptOrder){
        String receiptOrderNo = receiptOrderService.addReceiptOrder(receiptOrder);
        return BaseResult.ok(receiptOrderNo);
    }

    /**
     * 修改入库单
     * @param receiptOrder 入库单信息
     * @return 修改结果
     */
    @PutMapping("/updateReceiptOrder")
    public BaseResult<?> updateReceiptOrder(@RequestBody ReceiptOrder receiptOrder){
        receiptOrderService.updateReceiptOrder(receiptOrder);
        return BaseResult.ok();
    }

    /**
     * 删除入库单
     * @param id 入库单id
     * @return 操作结果
     */
    @DeleteMapping("/deleteReceiptOrder")
    public BaseResult<?> deleteReceiptOrder(@RequestParam Long id){
        receiptOrderService.deleteReceiptOrder(id);
        return BaseResult.ok();
    }

    /**
     * 完成入库
     * 更新入库单状态为已入库
     * @param id 入库单id
     * @return 操作结果
     */
    @PostMapping("/completeReceipt")
    public BaseResult<?> completeReceipt(@RequestParam Long id){
        receiptOrderService.completeReceipt(id);
        return BaseResult.ok();
    }
}
