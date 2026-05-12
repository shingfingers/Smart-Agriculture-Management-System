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
 * 移库单详情实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_movement_order_detail")
public class MovementOrderDetail {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 移库单ID
     */
    private Long movementOrderId;

    /**
     * 规格ID
     */
    private Long skuId;

    /**
     * 移库数量
     */
    private BigDecimal quantity;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 过期日期
     */
    private LocalDateTime expirationDate;

    /**
     * 源仓库
     */
    private Long sourceWarehouseId;

    /**
     * 源库区
     */
    private Long sourceAreaId;

    /**
     * 目标仓库
     */
    private Long targetWarehouseId;

    /**
     * 目标库区
     */
    private Long targetAreaId;

    /**
     * 入库详情ID
     */
    private Long inventoryDetailId;

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