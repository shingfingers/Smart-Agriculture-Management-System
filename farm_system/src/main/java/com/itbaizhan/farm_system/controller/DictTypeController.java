package com.itbaizhan.farm_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_system.entity.DictType;
import com.itbaizhan.farm_system.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典类型管理控制器
 */
@RestController
@RequestMapping("/dictType")
public class DictTypeController {
    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 分页查询字典类型列表
     *
     * @param pageNum 当前页码，默认为1
     * @param pageSize 每页记录数，默认为10
     * @param dictName 字典名称，可选参数，支持模糊查询
     * @param status 字典状态，可选参数，0-正常 1-停用
     * @return 字典类型分页查询结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<DictType>> getDictTypeList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "dictName", required = false) String dictName,
            @RequestParam(value = "status", required = false) String status) {
        IPage<DictType> result = dictTypeService.findDictTypePage(pageNum, pageSize, dictName, status);
        return BaseResult.ok(result);
    }

    /**
     * 根据ID查询字典类型详情
     *
     * @param dictId 字典类型ID，必填参数
     * @return 字典类型详情信息
     */
    @GetMapping("/getDictTypeById")
    public BaseResult<DictType> getDictTypeById(@RequestParam("dictId") Long dictId) {
        DictType dictType = dictTypeService.findById(dictId);
        return BaseResult.ok(dictType);
    }

    /**
     * 新增字典类型
     * @param dictType 字典类型信息，JSON格式，必须通过数据校验
     * @return 操作结果
     */
    @PostMapping("/addDictType")
    public BaseResult addDictType(@RequestBody @Validated DictType dictType) {
        dictTypeService.addDictType(dictType);
        return BaseResult.ok();
    }

    /**
     * 修改字典类型
     * @param dictType 字典类型信息，JSON格式，必须包含dictId，必须通过数据校验
     * @return 操作结果
     */
    @PutMapping("/updateDictType")
    public BaseResult updateDictType(@RequestBody @Validated DictType dictType) {
        dictTypeService.updateDictType(dictType);
        return BaseResult.ok();
    }

    /**
     * 批量删除字典类型
     * @param dictIds 字典类型ID列表，多个ID用逗号分隔
     * @return 操作结果
     */
    @DeleteMapping("/deleteDictType")
    public BaseResult deleteDictType(@RequestParam("dictIds") String dictIds) {
        List<Long> idList = Arrays.stream(dictIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        dictTypeService.deleteDictType(idList);
        return BaseResult.ok();
    }

    /**
     * 查询所有有效的字典类型
     * @return 有效字典类型列表
     */
    @GetMapping("/getAllDictTypes")
    public BaseResult<List<DictType>> getAllDictTypes() {
        List<DictType> result = dictTypeService.findAll();
        return BaseResult.ok(result);
    }
}

