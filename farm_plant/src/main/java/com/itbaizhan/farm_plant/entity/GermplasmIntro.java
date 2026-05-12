package com.itbaizhan.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 种质介绍表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_germplasm_intro")
public class GermplasmIntro {

    /**
     * 介绍ID
     */
    @TableId(value = "intro_id", type = IdType.AUTO)
    private Long introId;

    /**
     * 种质ID
     */
    private Long germplasmId;

    /**
     * 名称
     */
    private String introName;

    /**
     * 描述
     */
    private String introDes;

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