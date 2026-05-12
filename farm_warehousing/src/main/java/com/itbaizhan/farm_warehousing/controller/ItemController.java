package com.itbaizhan.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_warehousing.entity.Item;
import com.itbaizhan.farm_warehousing.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料Controller
 */
@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 分页查询物料列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param itemName 物料名称，可选
     * @param itemCode 物料编码，可选
     * @param categoryId 物料分类ID，可选
     * @param brandId 品牌ID，可选
     * @return 物料分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Item>> getItemList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "itemName", required = false) String itemName,
            @RequestParam(value = "itemCode", required = false) String itemCode,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "brandId", required = false) Long brandId) {
        IPage<Item> itemPage = itemService.findItemPage(pageNum, pageSize, itemName, itemCode, categoryId, brandId);
        return BaseResult.ok(itemPage);
    }

    /**
     * 根据id查询物料详情
     * @param id 物料ID
     * @return 物料详情
     */
    @GetMapping("/getItemById")
    public BaseResult<Item> findById(@RequestParam("id") Long id) {
        Item item = itemService.findById(id);
        return BaseResult.ok(item);
    }

    /**
     * 新增物料
     * @param item 物料信息
     * @return 操作结果
     */
    @PostMapping("/addItem")
    public BaseResult<?> addItem(@RequestBody Item item) {
        itemService.addItem(item);
        return BaseResult.ok();
    }

    /**
     * 修改物料
     * @param item 物料信息
     * @return 操作结果
     */
    @PutMapping("/updateItem")
    public BaseResult<?> updateItem(@RequestBody Item item) {
        itemService.updateItem(item);
        return BaseResult.ok();
    }

    /**
     * 删除物料
     * @param ids 物料ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteItem")
    public BaseResult<?> deleteItem(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        itemService.deleteItem(idList);
        return BaseResult.ok();
    }
}

