package com.itbaizhan.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_warehousing.entity.ItemCategory;
import com.itbaizhan.farm_warehousing.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料类型Controller
 */
@RestController
@RequestMapping("/itemCategory")
public class ItemCategoryController {
    @Autowired
    private ItemCategoryService itemCategoryService;

    /**
     * 分页查询物料类型列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param categoryName 物料类型名称，可选
     * @param parentId 父类型ID
     * @return 物料类型分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<ItemCategory>> getItemCategoryList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "parentId", required = false) Long parentId) {
        IPage<ItemCategory> itemCategoryPage = itemCategoryService.findItemCategoryPage(pageNum, pageSize, categoryName,parentId);
        return BaseResult.ok(itemCategoryPage);
    }

    /**
     * 根据id查询物料类型详情
     * @param id 物料类型ID
     * @return 物料类型详情
     */
    @GetMapping("/getItemCategoryById")
    public BaseResult<ItemCategory> findById(@RequestParam("id") Long id) {
        ItemCategory itemCategory = itemCategoryService.findById(id);
        return BaseResult.ok(itemCategory);
    }

    /**
     * 根据父类型ID查询物料类型
     * @param parentId 父类型ID
     * @return 物料类型列表
     */
    @GetMapping("/getByParentId")
    public BaseResult<List<ItemCategory>> findByParentId(@RequestParam("parentId") Long parentId) {
        List<ItemCategory> itemCategoryList = itemCategoryService.findByParentId(parentId);
        return BaseResult.ok(itemCategoryList);
    }

    /**
     * 新增物料类型
     * @param itemCategory 物料类型信息
     * @return 操作结果
     */
    @PostMapping("/addItemCategory")
    public BaseResult<?> addItemCategory(@RequestBody ItemCategory itemCategory) {
        itemCategoryService.addItemCategory(itemCategory);
        return BaseResult.ok();
    }

    /**
     * 修改物料类型
     * @param itemCategory 物料类型信息
     * @return 操作结果
     */
    @PutMapping("/updateItemCategory")
    public BaseResult<?> updateItemCategory(@RequestBody ItemCategory itemCategory) {
        itemCategoryService.updateItemCategory(itemCategory);
        return BaseResult.ok();
    }

    /**
     * 删除物料类型
     * @param ids 物料类型ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteItemCategory")
    public BaseResult<?> deleteItemCategory(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        itemCategoryService.deleteItemCategory(idList);
        return BaseResult.ok();
    }
}

