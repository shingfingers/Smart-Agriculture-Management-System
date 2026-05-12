package com.itbaizhan.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 农资类别表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_material_type")
public class MaterialType {

    /**
     * 农资类别ID
     */
    @TableId(value = "material_type_id", type = IdType.AUTO)
    private Long materialTypeId;

    /**
     * 农资类别名称
     */
    private String materialTypeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序
     */
    private Integer orderNum;

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