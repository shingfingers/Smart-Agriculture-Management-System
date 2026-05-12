package com.itbaizhan.farm_warehousing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物料规格实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_item_sku")
public class ItemSku {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 物料ID
     */
    private Long itemId;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 规格名称
     */
    private String skuName;

    /**
     * 规格条码
     */
    private String barcode;

    /**
     * 售价
     */
    private BigDecimal sellingPrice;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 毛重（kg）
     */
    private BigDecimal grossWeight;

    /**
     * 净重（kg）
     */
    private BigDecimal netWeight;

    /**
     * 长度（cm）
     */
    private BigDecimal length;

    /**
     * 宽度（cm）
     */
    private BigDecimal width;

    /**
     * 高度（cm）
     */
    private BigDecimal height;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
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
     * 更新时间
     */
    private LocalDateTime updateTime;
}