package com.itbaizhan.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_plant.entity.Machine;
import com.itbaizhan.farm_plant.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农机信息Controller
 */
@RestController
@RequestMapping("/machine")
public class MachineController {
    @Autowired
    private MachineService machineService;

    /**
     * 分页查询农机信息列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param machineName 农机名称，可选
     * @param machineTypeId 农机类别ID，可选
     * @return 农机信息分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Machine>> getMachineList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "machineName", required = false) String machineName,
            @RequestParam(value = "machineTypeId", required = false) Long machineTypeId) {
        IPage<Machine> page = machineService.findMachinePage(pageNum, pageSize, machineName, machineTypeId);
        return BaseResult.ok(page);
    }

    /**
     * 根据id查询农机信息详情
     *
     * @param id 农机信息ID
     * @return 农机信息详情
     */
    @GetMapping("/getMachineById")
    public BaseResult<Machine> findById(@RequestParam("id") Long id) {
        Machine machine = machineService.findById(id);
        return BaseResult.ok(machine);
    }

    /**
     * 新增农机信息
     *
     * @param machine 农机信息
     * @return 操作结果
     */
    @PostMapping("/addMachine")
    public BaseResult<?> addMachine(@RequestBody Machine machine) {
        machineService.addMachine(machine);
        return BaseResult.ok();
    }

    /**
     * 修改农机信息
     *
     * @param machine 农机信息
     * @return 操作结果
     */
    @PutMapping("/updateMachine")
    public BaseResult<?> updateMachine(@RequestBody Machine machine) {
        machineService.updateMachine(machine);
        return BaseResult.ok();
    }

    /**
     * 删除农机信息
     *
     * @param ids 农机信息ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteMachine")
    public BaseResult<?> deleteMachine(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        machineService.deleteMachine(idList);
        return BaseResult.ok();
    }
}

