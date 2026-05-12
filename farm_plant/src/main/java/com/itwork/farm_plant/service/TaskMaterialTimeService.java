package com.itwork.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_plant.entity.TaskMaterialTime;
import com.itwork.farm_plant.mapper.TaskMaterialTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务农资用量服务
 */
@Service
@Transactional
public class TaskMaterialTimeService {
    @Autowired
    private TaskMaterialTimeMapper taskMaterialTimeMapper;

    /**
     * 分页查询农资用量
     *
     * @param page 页码
     * @param size 每页数量
     * @param taskId 任务ID，可选
     * @param materialId 农资ID，可选
     * @return 农资用量分页结果
     */
    public IPage<TaskMaterialTime> findTaskMaterialTimePage(int page, int size, Long taskId, Long materialId) {
        Page<TaskMaterialTime> pageObj = new Page<>(page, size);
        QueryWrapper<TaskMaterialTime> queryWrapper = new QueryWrapper<>();
        if (taskId != null) {
            queryWrapper.eq("task_id", taskId);
        }
        if (materialId != null) {
            queryWrapper.eq("material_id", materialId);
        }
        queryWrapper.orderByDesc("create_time");
        return taskMaterialTimeMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据ID查询农资用量
     *
     * @param id 农资用量ID
     * @return 农资用量实体
     */
    public TaskMaterialTime findById(Long id) {
        return taskMaterialTimeMapper.selectById(id);
    }

    /**
     * 添加农资用量
     *
     * @param taskMaterialTime 农资用量实体
     * @return 添加结果
     */
    public boolean addTaskMaterialTime(TaskMaterialTime taskMaterialTime) {
        if (taskMaterialTime == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        String username = SecurityUtil.getUserName();
        taskMaterialTime.setCreateTime(LocalDateTime.now());
        taskMaterialTime.setCreateBy(username);
        taskMaterialTime.setUpdateTime(LocalDateTime.now());
        taskMaterialTime.setUpdateBy(username);
        return taskMaterialTimeMapper.insert(taskMaterialTime) > 0;
    }

    /**
     * 修改农资用量
     *
     * @param taskMaterialTime 农资用量实体
     * @return 修改结果
     */
    public boolean updateTaskMaterialTime(TaskMaterialTime taskMaterialTime) {
        if (taskMaterialTime == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        String username = SecurityUtil.getUserName();
        taskMaterialTime.setUpdateTime(LocalDateTime.now());
        taskMaterialTime.setUpdateBy(username);
        return taskMaterialTimeMapper.updateById(taskMaterialTime) > 0;
    }

    /**
     * 删除农资用量
     *
     * @param ids 农资用量ID列表
     * @return 删除结果
     */
    public boolean deleteTaskMaterialTime(List<Long> ids) {
        return taskMaterialTimeMapper.deleteBatchIds(ids) > 0;
    }
}

