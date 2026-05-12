package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.Employee;
import com.itbaizhan.farm_plant.entity.Task;
import com.itbaizhan.farm_plant.mapper.EmployeeMapper;
import com.itbaizhan.farm_plant.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 作业任务Service
 */
@Service
@Transactional
public class TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 分页查询种植任务
     *
     * @param page 当前页码
     * @param size 每页数量
     * @param taskName 任务名称,可选
     * @param batchId 批次ID,可选
     * @param status 任务状态,可选
     * @return 种植任务分页数据
     */
    public IPage<Task> findTaskPage(int page, int size, String taskName, Long batchId, String status) {
        Page<Task> pageObj = new Page<>(page, size);
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(taskName)) {
            queryWrapper.like("task_name", taskName);
        }
        if (batchId != null) {
            queryWrapper.eq("batch_id", batchId);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");
        return taskMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据负责人查询种植任务
     *
     * @param page 当前页码
     * @param size 每页数量
     * @param taskHead 负责人ID,必填
     * @param taskName 任务名称,可选
     * @param batchId 批次ID,可选
     * @param status 任务状态,可选
     * @return 种植任务分页数据
     */
    public IPage<Task> finUserTaskPage(int page, int size, Long taskHead, String taskName, Long batchId, String status) {
        Page<Task> pageObj = new Page<>(page, size);
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_head", taskHead);
        if (StringUtils.hasText(taskName)) {
            queryWrapper.like("task_name", taskName);
        }
        if (batchId != null) {
            queryWrapper.eq("batch_id", batchId);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");
        return taskMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据任务ID查询任务,包括任务的雇员信息
     *
     * @param id 任务ID
     * @return 任务对象
     */
    public Task findById(Long id) {
        Task task = taskMapper.selectById(id);
        if (task != null) {
            task.setEmployees(getEmployeeByTaskId(id));
        }
        return task;
    }

    /**
     * 添加任务
     *
     * @param task 任务对象
     * @return 添加结果
     */
    public boolean addTask(Task task) {
        if (task == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        task.setStatus("0");
        String username = SecurityUtil.getUserName();
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setCreateBy(username);
        task.setUpdateBy(username);
        return taskMapper.insert(task) > 0;
    }

    /**
     * 修改任务
     *
     * @param task 待修改任务对象
     * @return 修改结果
     */
    public boolean updateTask(Task task) {
        if (task == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        String username = SecurityUtil.getUserName();
        task.setUpdateTime(LocalDateTime.now());
        task.setUpdateBy(username);
        return taskMapper.updateById(task) > 0;
    }

    /**
     * 删除任务
     *
     * @param ids 任务ID列表
     * @return 删除结果
     */
    public boolean deleteTask(List<Long> ids) {
        for (Long id : ids) {
            removeAllTaskEmployees(id);
        }
        return taskMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 删除任务下的所有雇员关联关系
     *
     * @param taskId 任务ID
     * @return 删除结果
     */
    public boolean removeAllTaskEmployees(Long taskId) {
        return taskMapper.deleteAllTaskEmployees(taskId) > 0;
    }

    /**
     * 根据种植任务ID获取参与任务的雇员列表
     *
     * @param taskId 任务ID
     * @return 雇员列表
     */
    public List<Employee> getEmployeeByTaskId(Long taskId) {
        List<Long> employeeIds = taskMapper.getEmployeeIdsByTaskId(taskId);
        if (employeeIds == null || employeeIds.isEmpty()) {
            return List.of();
        }
        return employeeMapper.selectBatchIds(employeeIds);
    }

    /**
     * 为种植任务添加雇员
     *
     * @param taskId 种植任务ID
     * @param employeeIds 雇员ID列表
     * @return 添加结果
     */
    public boolean addTaskEmployees(Long taskId, List<Long> employeeIds) {
        String username = SecurityUtil.getUserName();
        LocalDateTime now = LocalDateTime.now();
        for (Long employeeId : employeeIds) {
            taskMapper.insertTaskEmployees(taskId, employeeId, username, now, username, now);
        }
        return true;
    }

    /**
     * 为种植任务删除雇员
     *
     * @param taskId 种植任务ID
     * @param employeeIds 雇员ID列表
     * @return 删除结果
     */
    public boolean deleteTaskEmployee(Long taskId, List<Long> employeeIds) {
        for (Long employeeId : employeeIds) {
            taskMapper.deleteTaskEmployee(taskId, employeeId);
        }
        return true;
    }
}

