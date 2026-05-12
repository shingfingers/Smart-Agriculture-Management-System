package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.GermplasmMethod;
import com.itwork.farm_plant.service.GermplasmMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 种植方法Controller
 */
@RestController
@RequestMapping("/germplasmMethod")
public class GermplasmMethodController {
    @Autowired
    private GermplasmMethodService germplasmMethodService;

    /**
     * 分页查询种植方法列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param methodName 方法名称，可选
     * @param germplasmId 种质ID，可选
     * @return 种植方法分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<GermplasmMethod>> getGermplasmMethodList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "methodName", required = false) String methodName,
            @RequestParam(value = "germplasmId", required = false) Long germplasmId) {
        IPage<GermplasmMethod> page = germplasmMethodService.findGermplasmMethodPage(pageNum, pageSize, methodName, germplasmId);
        return BaseResult.ok(page);
    }

    /**
     * 根据id查询种植方法详情
     *
     * @param id 种植方法ID
     * @return 种植方法详情
     */
    @GetMapping("/getGermplasmMethodById")
    public BaseResult<GermplasmMethod> findById(@RequestParam("id") Long id) {
        GermplasmMethod germplasmMethod = germplasmMethodService.findById(id);
        return BaseResult.ok(germplasmMethod);
    }

    /**
     * 新增种植方法
     *
     * @param germplasmMethod 种植方法信息
     * @return 操作结果
     */
    @PostMapping("/addGermplasmMethod")
    public BaseResult<?> addGermplasmMethod(@RequestBody GermplasmMethod germplasmMethod) {
        germplasmMethodService.addGermplasmMethod(germplasmMethod);
        return BaseResult.ok();
    }

    /**
     * 修改种植方法
     *
     * @param germplasmMethod 种植方法信息
     * @return 操作结果
     */
    @PutMapping("/updateGermplasmMethod")
    public BaseResult<?> updateGermplasmMethod(@RequestBody GermplasmMethod germplasmMethod) {
        germplasmMethodService.updateGermplasmMethod(germplasmMethod);
        return BaseResult.ok();
    }

    /**
     * 删除种植方法
     *
     * @param ids 种植方法ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteGermplasmMethod")
    public BaseResult<?> deleteGermplasmMethod(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        germplasmMethodService.deleteGermplasmMethod(idList);
        return BaseResult.ok();
    }
}

