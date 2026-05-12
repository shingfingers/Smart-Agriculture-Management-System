package com.itbaizhan.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_warehousing.entity.Merchant;
import com.itbaizhan.farm_warehousing.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 往来单位Controller
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    /**
     * 分页查询往来单位列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param merchantName 往来单位名称，可选
     * @param merchantCode 往来单位编码
     * @param merchantType 往来单位类型，可选
     * @return 往来单位分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Merchant>> getMerchantList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "merchantName", required = false) String merchantName,
            @RequestParam(value = "merchantCode", required = false) String merchantCode,
            @RequestParam(value = "merchantType", required = false) Integer merchantType) {
        IPage<Merchant> merchantPage = merchantService.findMerchantPage(pageNum, pageSize, merchantName, merchantCode,merchantType);
        return BaseResult.ok(merchantPage);
    }

    /**
     * 根据id查询往来单位详情
     * @param id 往来单位ID
     * @return 往来单位详情
     */
    @GetMapping("/getMerchantById")
    public BaseResult<Merchant> findById(@RequestParam("id") Long id) {
        Merchant merchant = merchantService.findById(id);
        return BaseResult.ok(merchant);
    }

    /**
     * 新增往来单位
     * @param merchant 往来单位信息
     * @return 操作结果
     */
    @PostMapping("/addMerchant")
    public BaseResult<?> addMerchant(@RequestBody Merchant merchant) {
        merchantService.addMerchant(merchant);
        return BaseResult.ok();
    }

    /**
     * 修改往来单位
     * @param merchant 往来单位信息
     * @return 操作结果
     */
    @PutMapping("/updateMerchant")
    public BaseResult<?> updateMerchant(@RequestBody Merchant merchant) {
        merchantService.updateMerchant(merchant);
        return BaseResult.ok();
    }

    /**
     * 删除往来单位
     * @param ids 往来单位ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteMerchant")
    public BaseResult<?> deleteMerchant(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        merchantService.deleteMerchant(idList);
        return BaseResult.ok();
    }
}

