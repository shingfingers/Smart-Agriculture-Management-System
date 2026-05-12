package com.itbaizhan.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_warehousing.entity.CheckOrderDetail;
import com.itbaizhan.farm_warehousing.service.CheckOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 盘库单详情控制层
 */
@RestController
@RequestMapping("/checkOrderDetail")
public class CheckOrderDetailController {
    @Autowired
    private CheckOrderDetailService checkOrderDetailService;

    /**
     * 查询盘库单详情分页列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param checkOrderId 盘库单id，可选
     * @return 盘库单详情分页列表
     */
    @GetMapping("/list")
    public BaseResult<IPage<CheckOrderDetail>> list(
        @RequestParam(defaultValue = "1")Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false)Long checkOrderId){
        IPage<CheckOrderDetail> checkOrderDetailIPage = checkOrderDetailService.selectCheckOrderDetailPage(pageNo, pageSize, checkOrderId);
        return BaseResult.ok(checkOrderDetailIPage);
    }

    /**
     * 根据ID查询盘库单详情
     * @param id 盘库单详情ID
     * @return 盘库单详情
     */
    @GetMapping("/getCheckOrderDetailById")
    public BaseResult<CheckOrderDetail> getCheckOrderDetailById(@RequestParam Long id){
        CheckOrderDetail checkOrderDetail = checkOrderDetailService.getCheckOrderDetailById(id);
        return BaseResult.ok(checkOrderDetail);
    }

    /**
     * 添加盘库单详情
     * @param checkOrderDetail 盘库单详情
     * @return 添加结果
     */
    @PostMapping("/addCheckOrderDetail")
    public BaseResult<?> addCheckOrderDetail(@RequestBody CheckOrderDetail checkOrderDetail){
        checkOrderDetailService.addCheckOrderDetail(checkOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 修改盘库单详情
     * @param checkOrderDetail 盘库单详情
     * @return 修改结果
     */
    @PutMapping("/updateCheckOrderDetail")
    public BaseResult<?> updateCheckOrderDetail(@RequestBody CheckOrderDetail checkOrderDetail){
        checkOrderDetailService.updateCheckOrderDetail(checkOrderDetail);
        return BaseResult.ok();
    }

    /**
     * 删除盘库单详情
     * @param ids 盘库单详情id，多个id用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteCheckOrderDetail")
    public BaseResult<?> deleteCheckOrderDetail(@RequestParam String ids){
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        checkOrderDetailService.deleteCheckOrderDetail(idList);
        return BaseResult.ok();
    }
}

