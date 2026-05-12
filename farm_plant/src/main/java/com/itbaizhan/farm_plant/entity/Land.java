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
 * 地块表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_land")
public class Land {

    /**
     * 地块ID
     */
    @TableId(value = "land_id", type = IdType.AUTO)
    private Long landId;

    /**
     * 地块名称
     */
    private String landName;

    /**
     * 地块类型 0:耕地 1：大棚 2：果园
     */
    private String landType;

    /**
     * 地块面积
     */
    private BigDecimal landArea;

    /**
     * 边框宽度
     */
    private Integer strokeWeight;

    /**
     * 边框颜色
     */
    private String strokeColor;

    /**
     * 边框透明度
     */
    private BigDecimal strokeOpacity;

    /**
     * 地块路径
     */
    private String landPath;

    /**
     * 地块背景颜色
     */
    private String fillColor;

    /**
     * 地块透明度
     */
    private BigDecimal fillOpacity;

    /**
     * 当前种植批次
     */
    private Long currentBatch;

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