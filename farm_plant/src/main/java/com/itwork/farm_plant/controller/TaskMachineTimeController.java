package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.TaskMachineTime;
import com.itwork.farm_plant.service.TaskMachineTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机械工时管理控制器
 */
@RestController
@RequestMapping("/taskMachineTime")
public class TaskMachineTimeController {
    @Autowired
    private TaskMachineTimeService taskMachineTimeService;

    /**
     * 分页查询机械工时列表
     *
     * @param pageNum 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param taskId 任务ID，可选
     * @param machineId 机械ID，可选
     * @return 机械工时分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<TaskMachineTime>> getTaskTimeList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "taskId", required = false) Long taskId,
            @RequestParam(value = "machineId", required = false) Long machineId) {
        IPage<TaskMachineTime> page = taskMachineTimeService.findTaskMachineTimePage(pageNum, pageSize, taskId, machineId);
        return BaseResult.ok(page);
    }

    /**
     * 根据ID查询机械工时信息
     *
     * @param id 机械工时ID
     * @return 机械工时信息
     */
    @GetMapping("/getTaskMachineTimeById")
    public BaseResult<TaskMachineTime> getTaskMachineTimeById(@RequestParam("id") Long id) {
        TaskMachineTime taskMachineTime = taskMachineTimeService.findById(id);
        return BaseResult.ok(taskMachineTime);
    }

    /**
     * 添加机械工时
     *
     * @param taskMachineTime 机械工时信息
     * @return 添加结果
     */
    @PostMapping("/addTaskMachineTime")
    public BaseResult<?> add(@RequestBody TaskMachineTime taskMachineTime) {
        taskMachineTimeService.add(taskMachineTime);
        return BaseResult.ok();
    }

    /**
     * 修改机械工时
     *
     * @param taskMachineTime 机械工时信息
     * @return 修改结果
     */
    @PutMapping("/updateTaskMachineTime")
    public BaseResult<?> update(@RequestBody TaskMachineTime taskMachineTime) {
        taskMachineTimeService.update(taskMachineTime);
        return BaseResult.ok();
    }

    /**
     * 删除机械工时
     *
     * @param ids 机械工时ID字符串，多个ID用逗号分割
     * @return 删除结果
     */
    @DeleteMapping("/deleteTaskMachineTime")
    public BaseResult<?> delete(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskMachineTimeService.delete(idList);
        return BaseResult.ok();
    }
}

