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
 * 盘库单详情实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_check_order_detail")
public class CheckOrderDetail {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 盘库单ID
     */
    private Long checkOrderId;

    /**
     * 规格ID
     */
    private Long skuId;

    /**
     * 库存数量（系统数量）
     */
    private BigDecimal quantity;

    /**
     * 盘点数量（实际数量）
     */
    private BigDecimal checkQuantity;

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
     * 过期日期
     */
    private LocalDateTime expirationDate;

    /**
     * 入库记录ID
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