package com.itwork.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 机械类别表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_machine_type")
public class MachineType {

    /**
     * 机械类别ID
     */
    @TableId(value = "machine_type_id", type = IdType.AUTO)
    private Long machineTypeId;

    /**
     * 机械类别名称
     */
    private String machineTypeName;

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