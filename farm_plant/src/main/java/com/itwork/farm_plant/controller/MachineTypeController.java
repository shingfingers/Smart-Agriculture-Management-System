package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.MachineType;
import com.itwork.farm_plant.service.MachineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农机类别Controller
 */
@RestController
@RequestMapping("/machineType")
public class MachineTypeController {
    @Autowired
    private MachineTypeService machineTypeService;

    /**
     * 分页查询农机类别列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param machineTypeName 农机类别名称，可选
     * @return 农机类别分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<MachineType>> getMachineTypeList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "machineTypeName", required = false) String machineTypeName) {
        IPage<MachineType> page = machineTypeService.findMachineTypePage(pageNum, pageSize, machineTypeName);
        return BaseResult.ok(page);
    }

    /**
     * 根据id查询农机类别详情
     *
     * @param id 农机类别ID
     * @return 农机类别详情
     */
    @GetMapping("/getMachineTypeById")
    public BaseResult<MachineType> findById(@RequestParam("id") Long id) {
        MachineType machineType = machineTypeService.findById(id);
        return BaseResult.ok(machineType);
    }

    /**
     * 新增农机类别
     *
     * @param machineType 农机类别信息
     * @return 操作结果
     */
    @PostMapping("/addMachineType")
    public BaseResult<?> addMachineType(@RequestBody MachineType machineType) {
        machineTypeService.addMachineType(machineType);
        return BaseResult.ok();
    }

    /**
     * 修改农机类别
     *
     * @param machineType 农机类别信息
     * @return 操作结果
     */
    @PutMapping("/updateMachineType")
    public BaseResult<?> updateMachineType(@RequestBody MachineType machineType) {
        machineTypeService.updateMachineType(machineType);
        return BaseResult.ok();
    }

    /**
     * 删除农机类别
     *
     * @param ids 农机类别ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteMachineType")
    public BaseResult<?> deleteMachineType(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        machineTypeService.deleteMachineType(idList);
        return BaseResult.ok();
    }
}

