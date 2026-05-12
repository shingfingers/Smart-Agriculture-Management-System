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
 * 出库单详情实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_shipment_order_detail")
public class ShipmentOrderDetail {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 出库单
     */
    private Long shipmentOrderId;

    /**
     * 规格ID
     */
    private Long skuId;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 过期时间
     */
    private LocalDateTime expirationDate;

    /**
     * 库存详情ID
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