package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.MovementOrderDetail;
import com.itwork.farm_warehousing.service.MovementOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 移库单详情控制层
 */
@RestController
@RequestMapping("/movementOrderDetail")
public class MovementOrderDetailController {
    @Autowired
    private MovementOrderDetailService movementOrderDetailService;

    /**
     * 查询移库单详情分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param movementOrderId 移库单id，可选
     * @return 移库单详情分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<MovementOrderDetail>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)Long movementOrderId){
        IPage<MovementOrderDetail> movementOrderDetailIPage = movementOrderDetailService.selectMovementOrderDetailPage(pageNo, pageSize, movementOrderId);
        return BaseResult.ok(movementOrderDetailIPage);
    }

    /**
     * 根据ID查询移库单详情
     * @param id 移库单详情ID
     * @return 移库单详情
     */
    @GetMapping("/getMovementOrderDetailById")
    public BaseResult<MovementOrderDetail> getMovementOrderDetailById(@RequestParam Long id){
        MovementOrderDetail movementOrderDetail = movementOrderDetailService.getMovementOrderDetailById(id);
        return BaseResult.ok(movementOrderDetail);
    }

    /**
     * 添加移库单详情
     * @param movementOrderDetail 移库单详情
     * @return 添加结果
     */
    @PostMapping("/addMovementOrderDetail")
    public BaseResult<?> addMovementOrderDetail(@RequestBody MovementOrderDetail movementOrderDetail){
        movementOrderDetailService.addMovementOrderDetail(movementOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 修改移库单详情
     * @param movementOrderDetail 移库单详情
     * @return 修改结果
     */
    @PutMapping("/updateMovementOrderDetail")
    public BaseResult<?> updateMovementOrderDetail(@RequestBody MovementOrderDetail movementOrderDetail){
        movementOrderDetailService.updateMovementOrderDetail(movementOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 删除移库单详情
     * @param ids 移库单详情id，多个id用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteMovementOrderDetail")
    public BaseResult<?> deleteMovementOrderDetail(@RequestParam String ids){
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        movementOrderDetailService.deleteMovementOrderDetail(idList);
        return BaseResult.ok();
    }
}

