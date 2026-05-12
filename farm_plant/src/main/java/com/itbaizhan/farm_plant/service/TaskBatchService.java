package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.Land;
import com.itbaizhan.farm_plant.mapper.LandMapper;
import com.itbaizhan.farm_plant.mapper.TaskBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itbaizhan.farm_plant.entity.TaskBatch;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 种植批次服务
 */
@Service
@Transactional
public class TaskBatchService {
    @Autowired
    private TaskBatchMapper taskBatchMapper;
    @Autowired
    private LandMapper landMapper;
    /**
     * 分页查询作物种植批次
     * @param page 当前页码
     * @param size 每页条数
     * @param batchName 批次名称,可选
     * @param germplasmId 种质ID,可选
     * @param landId 地块ID,可选
     * @return 分页结果
     */
    public IPage<TaskBatch> findTaskBatchPage(int page, int size, String batchName, Long germplasmId, Long landId){
        Page<TaskBatch> pageObj = new Page<>(page,size);
        QueryWrapper<TaskBatch> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(batchName)){
            queryWrapper.like("batch_name",batchName);
        }
        if (germplasmId != null){
            queryWrapper.eq("germplasm_id",germplasmId);
        }
        if (landId != null){
            queryWrapper.eq("land_id",landId);
        }
        queryWrapper.orderByDesc("create_time");

        return taskBatchMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 分页查询用户负责的作物种植批次
     * @param page 当前页码
     * @param size 每页条数
     * @param batchHeadId 负责人ID,必填
     * @param batchName 批次名称,可选
     * @return 分页结果
     */
    public IPage<TaskBatch> findUserTaskBatchPage(int page,int size,Long batchHeadId,String batchName){
        Page<TaskBatch> pageObj = new Page<>(page,size);
        QueryWrapper<TaskBatch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_head",batchHeadId);
        if (StringUtils.hasText(batchName)){
            queryWrapper.like("batch_name",batchName);
        }
        queryWrapper.orderByDesc("create_time");

        return taskBatchMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据ID查询种植批次
     * @param id 批次ID
     * @return 批次信息
     */
    public TaskBatch findId(Long id){
        return taskBatchMapper.selectById(id);
    }


    /**
     * 添加种植批次
     * @param taskBatch 批次信息
     * @return 是否添加成功
     */
    public boolean addTaskBatch(TaskBatch taskBatch){
        String username = SecurityUtil.getUserName();
        taskBatch.setCreateBy(username);
        taskBatch.setUpdateBy(username);
        taskBatch.setCreateTime(LocalDateTime.now());
        taskBatch.setUpdateTime(LocalDateTime.now());

        taskBatchMapper.insert(taskBatch);
        // 修改地块的种质批次
        Long landId = taskBatch.getLandId();
        Land land = landMapper.selectById(landId);
        land.setCurrentBatch(taskBatch.getBatchId());
        landMapper.updateById(land);

        return true;
    }

    /**
     * 修改种植批次
     * @param taskBatch 批次信息
     * @return 是否修改成功
     */
    public boolean updateTaskBatch(TaskBatch taskBatch){
        String username = SecurityUtil.getUserName();
        taskBatch.setUpdateBy(username);
        taskBatch.setUpdateTime(LocalDateTime.now());

        // 原来的地块，删除种质批次ID
        TaskBatch oldTaskBatch = taskBatchMapper.selectById(taskBatch.getBatchId());
        Land oldLand = landMapper.selectById(oldTaskBatch.getLandId());
        oldLand.setCurrentBatch(null);
        landMapper.updateById(oldLand);

        // 新的地块，设置种质批次ID
        Land newLand = landMapper.selectById(taskBatch.getLandId());
        newLand.setCurrentBatch(taskBatch.getBatchId());
        landMapper.updateById(newLand);

        return taskBatchMapper.updateById(taskBatch) > 0;
    }

    /**
     * 删除种植批次
     * @param ids 批次ID列表
     * @return 是否删除成功
     */
    public boolean deleteTaskBatch(List<Long> ids){
        // 删除地块的种质批次
        for (Long id : ids) {
            TaskBatch taskBatch = taskBatchMapper.selectById(id);
            Land land = landMapper.selectById(taskBatch.getLandId());
            land.setCurrentBatch(null);
            landMapper.updateById(land);
        }

        return taskBatchMapper.deleteBatchIds(ids) > 0;
    }
}
