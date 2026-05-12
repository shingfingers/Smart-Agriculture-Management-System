package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.TaskEmployeeTime;
import com.itwork.farm_plant.service.TaskEmployeeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人工工时控制器
 */
@RestController
@RequestMapping("/taskEmployeeTime")
public class TaskEmployeeTimeController {
    @Autowired
    private TaskEmployeeTimeService taskEmployeeTimeService;

    /**
     * 分页查询人工工时
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页数量，默认10
     * @param taskId 任务ID，可选
     * @param employeeId 雇员ID，可选
     * @return 人工工时分页数据
     */
    @GetMapping("/list")
    public BaseResult<IPage<TaskEmployeeTime>> getTaskTimeList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "taskId", required = false) Long taskId,
            @RequestParam(value = "employeeId", required = false) Long employeeId) {
        IPage<TaskEmployeeTime> page = taskEmployeeTimeService.findTaskEmployeeTimePage(pageNum, pageSize, taskId, employeeId);
        return BaseResult.ok(page);
    }

    /**
     * 根据ID查询人工工时详情
     *
     * @param id 工时ID
     * @return 人工工时详情
     */
    @GetMapping("/getTaskEmployeeTimeById")
    public BaseResult<TaskEmployeeTime> getTaskEmployeeTimeById(@RequestParam("id") Long id) {
        TaskEmployeeTime taskEmployeeTime = taskEmployeeTimeService.findById(id);
        return BaseResult.ok(taskEmployeeTime);
    }

    /**
     * 根据任务ID查询人工工时列表
     *
     * @param taskId 任务ID
     * @return 人工工时列表
     */
    @GetMapping("/getByTaskId")
    public BaseResult<List<TaskEmployeeTime>> getByTaskId(@RequestParam("taskId") Long taskId) {
        List<TaskEmployeeTime> list = taskEmployeeTimeService.findByTaskId(taskId);
        return BaseResult.ok(list);
    }

    /**
     * 新增人工工时
     *
     * @param taskEmployeeTime 人工工时信息
     * @return 操作结果
     */
    @PostMapping("/addTaskEmployeeTime")
    public BaseResult<?> add(@RequestBody TaskEmployeeTime taskEmployeeTime) {
        taskEmployeeTimeService.add(taskEmployeeTime);
        return BaseResult.ok();
    }

    /**
     * 修改人工工时
     *
     * @param taskEmployeeTime 人工工时信息
     * @return 操作结果
     */
    @PutMapping("/updateTaskEmployeeTime")
    public BaseResult<?> update(@RequestBody TaskEmployeeTime taskEmployeeTime) {
        taskEmployeeTimeService.update(taskEmployeeTime);
        return BaseResult.ok();
    }

    /**
     * 删除人工工时
     *
     * @param ids 工时ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteTaskEmployeeTime")
    public BaseResult<?> delete(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskEmployeeTimeService.delete(idList);
        return BaseResult.ok();
    }
}

