package com.itbaizhan.farm_warehousing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 出库单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_shipment_order")
public class ShipmentOrder {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 出库单号
     */
    private String shipmentOrderNo;

    /**
     * 出库类型(1.普通出库 2.盘亏出库)
     */
    private Integer shipmentOrderType;

    /**
     * 客户
     */
    private Long merchantId;

    /**
     * 订单金额
     */
    private BigDecimal receivableAmount;

    /**
     * 出库数量
     */
    private BigDecimal totalQuantity;

    /**
     * 出库单状态
     */
    private Integer shipmentOrderStatus;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 库区ID
     */
    private Long areaId;

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

    /**
     * 出库单详情列表
     */
    @TableField(exist = false)
    private List<ShipmentOrderDetail> detailList;
}
