package com.itwork.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_plant.entity.TaskEmployeeTime;
import com.itwork.farm_plant.mapper.TaskEmployeeTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 人工工时Service
 */
@Service
@Transactional
public class TaskEmployeeTimeService {
    @Autowired
    private TaskEmployeeTimeMapper taskEmployeeTimeMapper;

    /**
     * 分页查询人工工时
     *
     * @param page 当前页码
     * @param size 每页数量
     * @param taskId 任务ID，可选
     * @param employeeId 雇员ID，可选
     * @return 人工工时分页数据
     */
    public IPage<TaskEmployeeTime> findTaskEmployeeTimePage(int page, int size, Long taskId, Long employeeId) {
        Page<TaskEmployeeTime> pageObj = new Page<>(page, size);
        QueryWrapper<TaskEmployeeTime> queryWrapper = new QueryWrapper<>();
        if (taskId != null) {
            queryWrapper.eq("task_id", taskId);
        }
        if (employeeId != null) {
            queryWrapper.eq("employee_id", employeeId);
        }
        queryWrapper.orderByDesc("create_time");
        return taskEmployeeTimeMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据ID查询人工工时详情
     *
     * @param id 工时ID
     * @return 人工工时详情
     */
    public TaskEmployeeTime findById(Long id) {
        return taskEmployeeTimeMapper.selectById(id);
    }

    /**
     * 根据任务ID查询人工工时列表
     *
     * @param taskId 任务ID
     * @return 人工工时列表
     */
    public List<TaskEmployeeTime> findByTaskId(Long taskId) {
        QueryWrapper<TaskEmployeeTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        queryWrapper.orderByDesc("create_time");
        return taskEmployeeTimeMapper.selectList(queryWrapper);
    }

    /**
     * 新增人工工时
     *
     * @param taskEmployeeTime 人工工时信息
     * @return true成功，false失败
     */
    public boolean add(TaskEmployeeTime taskEmployeeTime) {
        if (taskEmployeeTime == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        String userName = SecurityUtil.getUserName();
        taskEmployeeTime.setCreateBy(userName);
        taskEmployeeTime.setUpdateBy(userName);
        taskEmployeeTime.setCreateTime(LocalDateTime.now());
        taskEmployeeTime.setUpdateTime(LocalDateTime.now());
        return taskEmployeeTimeMapper.insert(taskEmployeeTime) > 0;
    }

    /**
     * 修改人工工时
     *
     * @param taskEmployeeTime 人工工时信息
     * @return true成功，false失败
     */
    public boolean update(TaskEmployeeTime taskEmployeeTime) {
        if (taskEmployeeTime == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        taskEmployeeTime.setUpdateBy(SecurityUtil.getUserName());
        taskEmployeeTime.setUpdateTime(LocalDateTime.now());
        return taskEmployeeTimeMapper.updateById(taskEmployeeTime) > 0;
    }

    /**
     * 删除人工工时
     *
     * @param ids 工时ID列表
     * @return true成功，false失败
     */
    public boolean delete(List<Long> ids) {
        return taskEmployeeTimeMapper.deleteBatchIds(ids) > 0;
    }
}

