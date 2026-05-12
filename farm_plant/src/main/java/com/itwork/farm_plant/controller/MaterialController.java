package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.Material;
import com.itwork.farm_plant.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农资信息Controller
 */
@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    /**
     * 分页查询农资信息列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param materialName 农资名称，可选
     * @param materialTypeId 农资类别ID，可选
     * @return 农资信息分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Material>> getMaterialList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "materialName", required = false) String materialName,
            @RequestParam(value = "materialTypeId", required = false) Long materialTypeId) {
        IPage<Material> page = materialService.findMaterialPage(pageNum, pageSize, materialName, materialTypeId);
        return BaseResult.ok(page);
    }

    /**
     * 根据id查询农资信息详情
     *
     * @param id 农资信息ID
     * @return 农资信息详情
     */
    @GetMapping("/getMaterialById")
    public BaseResult<Material> findById(@RequestParam("id") Long id) {
        Material material = materialService.findById(id);
        return BaseResult.ok(material);
    }

    /**
     * 新增农资信息
     *
     * @param material 农资信息
     * @return 操作结果
     */
    @PostMapping("/addMaterial")
    public BaseResult<?> addMaterial(@RequestBody Material material) {
        materialService.addMaterial(material);
        return BaseResult.ok();
    }

    /**
     * 修改农资信息
     *
     * @param material 农资信息
     * @return 操作结果
     */
    @PutMapping("/updateMaterial")
    public BaseResult<?> updateMaterial(@RequestBody Material material) {
        materialService.updateMaterial(material);
        return BaseResult.ok();
    }

    /**
     * 删除农资信息
     *
     * @param ids 农资信息ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteMaterial")
    public BaseResult<?> deleteMaterial(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        materialService.deleteMaterial(idList);
        return BaseResult.ok();
    }
}

