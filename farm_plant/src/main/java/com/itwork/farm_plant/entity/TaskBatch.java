package com.itwork.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 任务批次表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_task_batch")
public class TaskBatch {

    /**
     * 批次ID
     */
    @TableId(value = "batch_id", type = IdType.AUTO)
    private Long batchId;

    /**
     * 批次名称
     */
    private String batchName;

    /**
     * 种质ID
     */
    private Long germplasmId;

    /**
     * 地块ID
     */
    private Long landId;

    /**
     * 种植面积（亩）
     */
    private BigDecimal cropArea;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 负责人id
     */
    private Long batchHead;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}