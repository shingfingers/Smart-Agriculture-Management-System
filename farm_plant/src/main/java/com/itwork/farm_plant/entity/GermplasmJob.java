package com.itwork.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 标准作业任务表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_germplasm_job")
public class GermplasmJob {

    /**
     * 作业任务ID
     */
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    /**
     * 种质ID
     */
    private Long germplasmId;

    /**
     * 作业任务名称
     */
    private String jobName;

    /**
     * 作业周期单位（0代表周 1代表天）
     */
    private String cycleUnit;

    /**
     * 起始周/天
     */
    private Integer jobStart;

    /**
     * 结束周/天
     */
    private Integer jobFinish;

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