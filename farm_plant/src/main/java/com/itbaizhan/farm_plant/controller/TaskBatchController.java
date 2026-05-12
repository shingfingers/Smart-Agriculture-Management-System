package com.itbaizhan.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_plant.entity.TaskBatch;
import com.itbaizhan.farm_plant.service.TaskBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 种植批次控制器
 */
@RestController
@RequestMapping("/taskBatch")
public class TaskBatchController {
    @Autowired
    private TaskBatchService taskBatchService;

    /**
     * 分页查询作物种植批次
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页条数，默认10
     * @param batchName 批次名称,可选
     * @param germplasmId 种质ID,可选
     * @param landId  地块ID,可选
     * @return 作物种植批次分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<TaskBatch>> list(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam (value = "batchName",required = false) String batchName,
            @RequestParam (value = "germplasmId",required = false) Long germplasmId,
            @RequestParam (value = "landId",required = false) Long landId){
        IPage<TaskBatch> taskBatchPage = taskBatchService.findTaskBatchPage(pageNum, pageSize, batchName, germplasmId, landId);
        return BaseResult.ok(taskBatchPage);
    }
    /**
     * 分页查询登录用户负责的作物种植批次
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页条数，默认10
     * @param batchName 批次名称,可选
     * @param batchHead 负责人ID,必填
     * @return 作物种植批次分页结果
     */
    @GetMapping("/userList")
    public BaseResult<IPage<TaskBatch>> userList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam (value = "batchName",required = false) String batchName,
            @RequestParam ("batchHead") Long batchHead){
        IPage<TaskBatch> taskBatchPage = taskBatchService.findUserTaskBatchPage(pageNum, pageSize, batchHead, batchName);
        return BaseResult.ok(taskBatchPage);
    }

    /**
     * 根据ID查询作物种植批次
     * @param id 作物种植批次ID
     * @return 作物种植批次
     */
    @GetMapping("/getTaskBatchById")
    public BaseResult<TaskBatch> getTaskBatchById(@RequestParam Long id){
        TaskBatch taskBatch = taskBatchService.findId(id);
        return BaseResult.ok(taskBatch);
    }

    /**
     * 添加作物种植批次
     * @param taskBatch 作物种植批次
     * @return 添加结果
     */
    @PostMapping("/addTaskBatch")
    public BaseResult<?> addTaskBatch(@RequestBody TaskBatch taskBatch){
        taskBatchService.addTaskBatch(taskBatch);
        return BaseResult.ok();
    }

    /**
     * 修改作物种植批次
     * @param taskBatch 作物种植批次
     * @return 修改结果
     */
    @PutMapping("/updateTaskBatch")
    public BaseResult<?> updateTaskBatch(@RequestBody TaskBatch taskBatch){
        taskBatchService.updateTaskBatch(taskBatch);
        return BaseResult.ok();
    }

    /**
     * 删除作物种植批次
     * @param ids 作物种植批次ID字符串，多个以逗号分隔
     * @return 删除结果
     */
    @DeleteMapping("/deleteTaskBatch")
    public BaseResult<?> deleteTaskBatch(@RequestParam("ids") String ids){
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskBatchService.deleteTaskBatch(idList);
        return BaseResult.ok();
    }
}
