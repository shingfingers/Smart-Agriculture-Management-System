package com.itbaizhan.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 农资信息表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_material")
public class Material {

    /**
     * 农资ID
     */
    @TableId(value = "material_id", type = IdType.AUTO)
    private Long materialId;

    /**
     * 农资编码
     */
    private String materialCode;

    /**
     * 农资名称
     */
    private String materialName;

    /**
     * 农资类别
     */
    private Long materialTypeId;

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