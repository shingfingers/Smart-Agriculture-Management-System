package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.ReceiptOrderDetail;
import com.itwork.farm_warehousing.service.ReceiptOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 入库单详情控制层
 */
@RestController
@RequestMapping("/receiptOrderDetail")
public class ReceiptOrderDetailController {
    @Autowired
    private ReceiptOrderDetailService receiptOrderDetailService;

    /**
     * 查询入库单详情分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param receiptOrderId 入库单id，可选
     * @return 入库单分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<ReceiptOrderDetail>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)Long receiptOrderId){
        IPage<ReceiptOrderDetail> receiptOrderDetailIPage = receiptOrderDetailService.selectReceiptOrderDetailPage(pageNo, pageSize, receiptOrderId);
        return BaseResult.ok(receiptOrderDetailIPage);
    }

    /**
     * 根据ID查询入库单详情
     * @param id 入库单详情ID
     * @return 入库单详情
     */
    @GetMapping("/getReceiptOrderDetailById")
    public BaseResult<ReceiptOrderDetail> getReceiptOrderDetailById(@RequestParam Long id){
        ReceiptOrderDetail receiptOrderDetail = receiptOrderDetailService.getReceiptOrderDetailById(id);
        return BaseResult.ok(receiptOrderDetail);
    }

    /**
     * 添加入库单详情
     * @param receiptOrderDetail 入库单详情
     * @return 添加结果
     */
    @PostMapping("/addReceiptOrderDetail")
    public BaseResult<?> addReceiptOrderDetail(@RequestBody ReceiptOrderDetail receiptOrderDetail){
        receiptOrderDetailService.addReceiptOrderDetail(receiptOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 修改入库单详情
     * @param receiptOrderDetail 入库单详情
     * @return 添加结果
     */
    @PutMapping("/updateReceiptOrderDetail")
    public BaseResult<?> updateReceiptOrderDetail(@RequestBody ReceiptOrderDetail receiptOrderDetail){
        receiptOrderDetailService.updateReceiptOrderDetail(receiptOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 删除入库单详情
     * @param ids 入库单详情id，多过分id用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteReceiptOrderDetail")
    public BaseResult<?> deleteReceiptOrderDetail(@RequestParam String ids){
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        receiptOrderDetailService.deleteReceiptOrderDetail(idList);
        return BaseResult.ok();
    }
}
