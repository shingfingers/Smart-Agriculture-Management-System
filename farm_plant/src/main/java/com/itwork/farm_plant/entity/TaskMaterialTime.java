package com.itwork.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 农资用量表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_task_material_time")
public class TaskMaterialTime {

    /**
     * ID
     */
    @TableId(value = "cost_id", type = IdType.AUTO)
    private Long costId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 农资ID
     */
    private Long materialId;

    /**
     * 使用数量
     */
    private Integer materialCount;

    /**
     * 计量单位
     */
    private String measureUnit;

    /**
     * 开始日期
     */
    private LocalDateTime workingStart;

    /**
     * 结束日期
     */
    private LocalDateTime workingFinish;

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