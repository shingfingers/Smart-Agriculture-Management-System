package com.itbaizhan.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 种植方法表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_germplasm_method")
public class GermplasmMethod {

    /**
     * 方法ID
     */
    @TableId(value = "method_id", type = IdType.AUTO)
    private Long methodId;

    /**
     * 种质ID
     */
    private Long germplasmId;

    /**
     * 名称
     */
    private String methodName;

    /**
     * 图片
     */
    private String methodImg;

    /**
     * 描述
     */
    private String methodDes;

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