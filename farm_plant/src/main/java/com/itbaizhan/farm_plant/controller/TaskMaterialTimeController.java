package com.itbaizhan.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_plant.entity.TaskMaterialTime;
import com.itbaizhan.farm_plant.service.TaskMaterialTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农资用量管理控制器
 */
@RestController
@RequestMapping("/taskMaterialTime")
public class TaskMaterialTimeController {
    @Autowired
    private TaskMaterialTimeService taskMaterialTimeService;

    /**
     * 分页查询农资用量列表
     *
     * @param pageNum 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param taskId 任务ID，可选
     * @param materialId 农资ID，可选
     * @return 农资用量分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<TaskMaterialTime>> getTaskTimeList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "taskId", required = false) Long taskId,
            @RequestParam(value = "materialId", required = false) Long materialId) {
        IPage<TaskMaterialTime> page = taskMaterialTimeService.findTaskMaterialTimePage(pageNum, pageSize, taskId, materialId);
        return BaseResult.ok(page);
    }

    /**
     * 根据ID查询农资用量信息
     *
     * @param id 农资用量ID
     * @return 农资用量信息
     */
    @GetMapping("/getTaskMaterialTimeById")
    public BaseResult<TaskMaterialTime> getTaskMaterialTimeById(@RequestParam("id") Long id) {
        TaskMaterialTime taskMaterialTime = taskMaterialTimeService.findById(id);
        return BaseResult.ok(taskMaterialTime);
    }

    /**
     * 添加农资用量
     *
     * @param taskMaterialTime 农资用量信息
     * @return 添加结果
     */
    @PostMapping("/addTaskMaterialTime")
    public BaseResult<?> add(@RequestBody TaskMaterialTime taskMaterialTime) {
        taskMaterialTimeService.addTaskMaterialTime(taskMaterialTime);
        return BaseResult.ok();
    }

    /**
     * 修改农资用量
     *
     * @param taskMaterialTime 农资用量信息
     * @return 修改结果
     */
    @PutMapping("/updateTaskMaterialTime")
    public BaseResult<?> update(@RequestBody TaskMaterialTime taskMaterialTime) {
        taskMaterialTimeService.updateTaskMaterialTime(taskMaterialTime);
        return BaseResult.ok();
    }

    /**
     * 删除农资用量
     *
     * @param ids 农资用量ID字符串，多个ID用逗号分割
     * @return 删除结果
     */
    @DeleteMapping("/deleteTaskMaterialTime")
    public BaseResult<?> delete(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskMaterialTimeService.deleteTaskMaterialTime(idList);
        return BaseResult.ok();
    }
}

