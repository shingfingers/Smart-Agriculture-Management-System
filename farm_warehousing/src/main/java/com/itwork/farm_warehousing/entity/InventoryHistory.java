package com.itwork.farm_warehousing.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_inventory_history")
public class InventoryHistory {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 物料ID
     */
    private Long skuId;

    /**
     * 库存变化
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
     * 金额
     */
    private BigDecimal amount;

    /**
     * 操作单ID（出库、入库、库存移动表单ID）
     */
    private Long orderId;

    /**
     * 操作单号（入库、出库、移库、盘库单号）
     */
    private String orderNo;

    /**
     * 操作类型（1.入库 2.出库 3.移库 4.盘库）
     */
    private Integer orderType;

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