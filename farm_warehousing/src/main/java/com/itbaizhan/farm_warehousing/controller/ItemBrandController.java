package com.itbaizhan.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_warehousing.entity.ItemBrand;
import com.itbaizhan.farm_warehousing.service.ItemBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品品牌Controller
 */
@RestController
@RequestMapping("/itemBrand")
public class ItemBrandController {
    @Autowired
    private ItemBrandService itemBrandService;

    /**
     * 分页查询商品品牌列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param brandName 品牌名称，可选
     * @return 商品品牌分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<ItemBrand>> getItemBrandList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "brandName", required = false) String brandName) {
        IPage<ItemBrand> itemBrandPage = itemBrandService.findItemBrandPage(pageNum, pageSize, brandName);
        return BaseResult.ok(itemBrandPage);
    }

    /**
     * 根据id查询商品品牌详情
     * @param id 商品品牌ID
     * @return 商品品牌详情
     */
    @GetMapping("/getItemBrandById")
    public BaseResult<ItemBrand> findById(@RequestParam("id") Long id) {
        ItemBrand itemBrand = itemBrandService.findById(id);
        return BaseResult.ok(itemBrand);
    }

    /**
     * 新增商品品牌
     * @param itemBrand 商品品牌信息
     * @return 操作结果
     */
    @PostMapping("/addItemBrand")
    public BaseResult<?> addItemBrand(@RequestBody ItemBrand itemBrand) {
        itemBrandService.addItemBrand(itemBrand);
        return BaseResult.ok();
    }

    /**
     * 修改商品品牌
     * @param itemBrand 商品品牌信息
     * @return 操作结果
     */
    @PutMapping("/updateItemBrand")
    public BaseResult<?> updateItemBrand(@RequestBody ItemBrand itemBrand) {
        itemBrandService.updateItemBrand(itemBrand);
        return BaseResult.ok();
    }

    /**
     * 删除商品品牌
     * @param ids 商品品牌ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteItemBrand")
    public BaseResult<?> deleteItemBrand(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        itemBrandService.deleteItemBrand(idList);
        return BaseResult.ok();
    }
}

