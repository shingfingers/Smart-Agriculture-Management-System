package com.itbaizhan.farm_plant.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 机械工时表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_task_machine_time")
public class TaskMachineTime {

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
     * 机械ID
     */
    private Long machineId;

    /**
     * 机械数量
     */
    private Integer machineCount;

    /**
     * 工时
     */
    private BigDecimal workingHours;

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