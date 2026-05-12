package com.itbaizhan.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_warehousing.entity.ItemSku;
import com.itbaizhan.farm_warehousing.service.ItemSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料规格Controller
 */
@RestController
@RequestMapping("/itemSku")
public class ItemSkuController {
    @Autowired
    private ItemSkuService itemSkuService;

    /**
     * 分页查询物料规格列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param skuName 规格名称，可选
     * @param skuCode 规格编码，可选
     * @param itemId 物料ID，可选
     * @return 物料规格分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<ItemSku>> getItemSkuList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "skuName", required = false) String skuName,
            @RequestParam(value = "skuCode", required = false) String skuCode,
            @RequestParam(value = "itemId", required = false) Long itemId) {
        IPage<ItemSku> itemSkuPage = itemSkuService.findItemSkuPage(pageNum, pageSize, skuName,skuCode,itemId);
        return BaseResult.ok(itemSkuPage);
    }

    /**
     * 根据id查询物料规格详情
     * @param id 物料规格ID
     * @return 物料规格详情
     */
    @GetMapping("/getItemSkuById")
    public BaseResult<ItemSku> findById(@RequestParam("id") Long id) {
        ItemSku itemSku = itemSkuService.findById(id);
        return BaseResult.ok(itemSku);
    }

    /**
     * 新增物料规格
     * @param itemSku 物料规格信息
     * @return 操作结果
     */
    @PostMapping("/addItemSku")
    public BaseResult<?> addItemSku(@RequestBody ItemSku itemSku) {
        itemSkuService.addItemSku(itemSku);
        return BaseResult.ok();
    }

    /**
     * 修改物料规格
     * @param itemSku 物料规格信息
     * @return 操作结果
     */
    @PutMapping("/updateItemSku")
    public BaseResult<?> updateItemSku(@RequestBody ItemSku itemSku) {
        itemSkuService.updateItemSku(itemSku);
        return BaseResult.ok();
    }

    /**
     * 删除物料规格
     * @param ids 物料规格ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteItemSku")
    public BaseResult<?> deleteItemSku(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        itemSkuService.deleteItemSku(idList);
        return BaseResult.ok();
    }
}

