package com.itwork.farm_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_system.entity.DictData;
import com.itwork.farm_system.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典数据管理控制器
 */
@RestController
@RequestMapping("/dictData")
public class DictDataController {
    @Autowired
    private DictDataService dictDataService;

    /**
     * 分页查询字典数据列表
     *
     * @param pageNum   当前页码，默认为1
     * @param pageSize  每页记录数，默认为10
     * @param dictLabel 字典标签，可选参数，支持模糊查询
     * @param dictType  字典类型ID，可选参数，精确匹配
     * @param status    字典状态，可选参数，0-正常 1-停用
     * @return 字典数据分页查询结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<DictData>> getDictDataList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "dictLabel", required = false) String dictLabel,
            @RequestParam(value = "dictType", required = false) Long dictType,
            @RequestParam(value = "status", required = false) String status) {
        IPage<DictData> result = dictDataService.findDictDataPage(pageNum, pageSize, dictLabel, dictType, status);
        return BaseResult.ok(result);
    }

    /**
     * 根据ID查询字典数据详情
     *
     * @param dictCode 字典数据编码，必填参数
     * @return 字典数据详情信息
     */
    @GetMapping("/getDictDataById")
    public BaseResult<DictData> getDictDataById(@RequestParam("dictCode") Long dictCode) {
        DictData dictData = dictDataService.findById(dictCode);
        return BaseResult.ok(dictData);
    }

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型ID，必填参数
     * @return 字典数据列表
     */
    @GetMapping("/getDictDataByType")
    public BaseResult<List<DictData>> getDictDataByType(@RequestParam("dictType") Long dictType) {
        List<DictData> result = dictDataService.findByDictType(dictType);
        return BaseResult.ok(result);
    }

    /**
     * 新增字典数据
     *
     * @param dictData 字典数据信息，JSON格式，必须通过数据校验
     * @return 操作结果
     */
    @PostMapping("/addDictData")
    public BaseResult addDictData(@RequestBody @Validated DictData dictData) {
        dictDataService.addDictData(dictData);
        return BaseResult.ok();
    }

    /**
     * 修改字典数据
     *
     * @param dictData 字典数据信息，JSON格式，必须包含dictCode，必须通过数据校验
     * @return 操作结果
     */
    @PutMapping("/updateDictData")
    public BaseResult updateDictData(@RequestBody @Validated DictData dictData) {
        dictDataService.updateDictData(dictData);
        return BaseResult.ok();
    }

    /**
     * 批量删除字典数据
     *
     * @param dictCodes 字典数据编码列表，多个编码用逗号分隔
     * @return 操作结果
     */
    @DeleteMapping("/deleteDictData")
    public BaseResult deleteDictData(@RequestParam("dictCodes") String dictCodes) {
        List<Long> idList = Arrays.stream(dictCodes.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        dictDataService.deleteDictData(idList);
        return BaseResult.ok();
    }
}
