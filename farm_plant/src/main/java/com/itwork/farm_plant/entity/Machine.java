package com.itwork.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 机械信息表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_machine")
public class Machine {

    /**
     * 机械ID
     */
    @TableId(value = "machine_id", type = IdType.AUTO)
    private Long machineId;

    /**
     * 机械编码
     */
    private String machineCode;

    /**
     * 机械名称
     */
    private String machineName;

    /**
     * 机械类别
     */
    private Long machineTypeId;

    /**
     * 计量单位
     */
    private String measureUnit;

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