package com.itbaizhan.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_plant.entity.MaterialType;
import com.itbaizhan.farm_plant.service.MaterialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农资类别Controller
 */
@RestController
@RequestMapping("/materialType")
public class MaterialTypeController {
    @Autowired
    private MaterialTypeService materialTypeService;

    /**
     * 分页查询农资类别列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param materialTypeName 农资类别名称，可选
     * @return 农资类别分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<MaterialType>> getMaterialTypeList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "materialTypeName", required = false) String materialTypeName) {
        IPage<MaterialType> page = materialTypeService.findMaterialTypePage(pageNum, pageSize, materialTypeName);
        return BaseResult.ok(page);
    }

    /**
     * 根据id查询农资类别详情
     *
     * @param id 农资类别ID
     * @return 农资类别详情
     */
    @GetMapping("/getMaterialTypeById")
    public BaseResult<MaterialType> findById(@RequestParam("id") Long id) {
        MaterialType materialType = materialTypeService.findById(id);
        return BaseResult.ok(materialType);
    }

    /**
     * 新增农资类别
     *
     * @param materialType 农资类别信息
     * @return 操作结果
     */
    @PostMapping("/addMaterialType")
    public BaseResult<?> addMaterialType(@RequestBody MaterialType materialType) {
        materialTypeService.addMaterialType(materialType);
        return BaseResult.ok();
    }

    /**
     * 修改农资类别
     *
     * @param materialType 农资类别信息
     * @return 操作结果
     */
    @PutMapping("/updateMaterialType")
    public BaseResult<?> updateMaterialType(@RequestBody MaterialType materialType) {
        materialTypeService.updateMaterialType(materialType);
        return BaseResult.ok();
    }

    /**
     * 删除农资类别
     *
     * @param ids 农资类别ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteMaterialType")
    public BaseResult<?> deleteMaterialType(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        materialTypeService.deleteMaterialType(idList);
        return BaseResult.ok();
    }
}

