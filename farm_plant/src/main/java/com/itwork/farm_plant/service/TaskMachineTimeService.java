package com.itwork.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_plant.entity.TaskMachineTime;
import com.itwork.farm_plant.mapper.TaskMachineTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机械工时表服务类
 */
@Service
@Transactional
public class TaskMachineTimeService {
    @Autowired
    private TaskMachineTimeMapper taskMachineTimeMapper;

    /**
     * 分页查询机械工时
     *
     * @param page 页码
     * @param size 每页数量
     * @param taskId 任务ID，可选
     * @param machineId 机械ID，可选
     * @return 机械工时分页结果
     */
    public IPage<TaskMachineTime> findTaskMachineTimePage(int page, int size, Long taskId, Long machineId) {
        Page<TaskMachineTime> pageObj = new Page<>(page, size);
        QueryWrapper<TaskMachineTime> queryWrapper = new QueryWrapper<>();
        if (taskId != null) {
            queryWrapper.eq("task_id", taskId);
        }
        if (machineId != null) {
            queryWrapper.eq("machine_id", machineId);
        }
        queryWrapper.orderByDesc("create_time");
        return taskMachineTimeMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据ID查询机械工时
     *
     * @param id 机械工时ID
     * @return 机械工时实体
     */
    public TaskMachineTime findById(Long id) {
        return taskMachineTimeMapper.selectById(id);
    }

    /**
     * 添加机械工时
     *
     * @param taskMachineTime 机械工时实体
     * @return 添加结果
     */
    public boolean add(TaskMachineTime taskMachineTime) {
        if (taskMachineTime == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        String username = SecurityUtil.getUserName();
        taskMachineTime.setCreateTime(LocalDateTime.now());
        taskMachineTime.setCreateBy(username);
        taskMachineTime.setUpdateTime(LocalDateTime.now());
        taskMachineTime.setUpdateBy(username);
        return taskMachineTimeMapper.insert(taskMachineTime) > 0;
    }

    /**
     * 修改机械工时
     *
     * @param taskMachineTime 机械工时实体
     * @return 修改结果
     */
    public boolean update(TaskMachineTime taskMachineTime) {
        if (taskMachineTime == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        String username = SecurityUtil.getUserName();
        taskMachineTime.setUpdateTime(LocalDateTime.now());
        taskMachineTime.setUpdateBy(username);
        return taskMachineTimeMapper.updateById(taskMachineTime) > 0;
    }

    /**
     * 删除机械工时
     *
     * @param ids 机械工时ID列表
     * @return 删除结果
     */
    public boolean delete(List<Long> ids) {
        return taskMachineTimeMapper.deleteBatchIds(ids) > 0;
    }
}

