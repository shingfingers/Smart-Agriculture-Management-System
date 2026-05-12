package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.Employee;
import com.itwork.farm_plant.entity.Task;
import com.itwork.farm_plant.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 种植任务控制器
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 分页查询种植任务
     *
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @param taskName 任务名称,可选
     * @param batchId 批次ID,可选
     * @param status 任务状态,可选
     * @return 种植任务分页数据
     */
    @GetMapping("list")
    public BaseResult<IPage<Task>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "taskName", required = false) String taskName,
            @RequestParam(value = "batchId", required = false) Long batchId,
            @RequestParam(value = "status", required = false) String status) {
        IPage<Task> taskPage = taskService.findTaskPage(pageNum, pageSize, taskName, batchId, status);
        return BaseResult.ok(taskPage);
    }

    /**
     * 根据负责人查询种植任务
     *
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @param taskHead 负责人ID,必填
     * @param taskName 任务名称,可选
     * @param batchId 批次ID,可选
     * @param status 任务状态,可选
     * @return 种植任务分页数据
     */
    @GetMapping("/userList")
    public BaseResult<IPage<Task>> userList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam("taskHead") Long taskHead,
            @RequestParam(value = "taskName", required = false) String taskName,
            @RequestParam(value = "batchId", required = false) Long batchId,
            @RequestParam(value = "status", required = false) String status) {
        IPage<Task> taskPage = taskService.finUserTaskPage(pageNum, pageSize, taskHead, taskName, batchId, status);
        return BaseResult.ok(taskPage);
    }

    /**
     * 根据任务ID查询任务
     *
     * @param id 任务ID
     * @return 任务信息
     */
    @GetMapping("/getTaskById")
    public BaseResult<Task> getTaskById(@RequestParam("id") Long id) {
        Task task = taskService.findById(id);
        return BaseResult.ok(task);
    }

    /**
     * 添加种植任务
     *
     * @param task 待添加任务对象
     * @return 添加结果
     */
    @PostMapping("/addTask")
    public BaseResult<?> addTask(@RequestBody Task task) {
        taskService.addTask(task);
        return BaseResult.ok();
    }

    /**
     * 修改种植任务
     *
     * @param task 待修改任务对象
     * @return 修改结果
     */
    @PutMapping("/updateTask")
    public BaseResult<?> updateTask(@RequestBody Task task) {
        taskService.updateTask(task);
        return BaseResult.ok();
    }

    /**
     * 删除种植任务
     *
     * @param ids 任务ID字符串，多个ID用逗号分割
     * @return 删除结果
     */
    @DeleteMapping("/deleteTask")
    public BaseResult<?> deleteTask(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskService.deleteTask(idList);
        return BaseResult.ok();
    }

    /**
     * 删除种植任务所有雇员
     *
     * @param taskId 任务ID
     * @return 删除结果
     */
    @DeleteMapping("/removeAllTaskEmployees")
    public BaseResult<?> removeAllTaskEmployees(@RequestParam("taskId") Long taskId) {
        taskService.removeAllTaskEmployees(taskId);
        return BaseResult.ok();
    }

    /**
     * 为种植任务添加雇员
     *
     * @param taskId 种植任务ID
     * @param employeeIds 雇员ID列表
     * @return 添加结果
     */
    @PostMapping("/addTaskEmployees")
    public BaseResult<?> addTaskEmployees(@RequestParam("taskId") Long taskId,
                                          @RequestParam("employeeIds") String employeeIds) {
        List<Long> employeeIdList = Arrays.stream(employeeIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskService.addTaskEmployees(taskId, employeeIdList);
        return BaseResult.ok();
    }

    /**
     * 为种植任务删除雇员
     *
     * @param taskId 种植任务ID
     * @param employeeIds 雇员ID列表
     * @return 删除结果
     */
    @DeleteMapping("/deleteTaskEmployees")
    public BaseResult<?> deleteTaskEmployees(@RequestParam("taskId") Long taskId,
                                             @RequestParam("employeeIds") String employeeIds) {
        List<Long> employeeIdList = Arrays.stream(employeeIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskService.deleteTaskEmployee(taskId, employeeIdList);
        return BaseResult.ok();
    }

    /**
     * 根据种植任务ID获取参与任务的雇员列表
     *
     * @param taskId 任务ID
     * @return 雇员列表
     */
    @GetMapping("/getTaskEmployees")
    public BaseResult<List<Employee>> getTaskEmployees(@RequestParam("taskId") Long taskId) {
        List<Employee> taskEmployees = taskService.getEmployeeByTaskId(taskId);
        return BaseResult.ok(taskEmployees);
    }
}

