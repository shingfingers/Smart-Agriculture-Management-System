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
 * 入库单详情实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_receipt_order_detail")
public class ReceiptOrderDetail {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 入库单ID
     */
    private Long receiptOrderId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 入库数量
     */
    private BigDecimal quantity;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 过期日期
     */
    private LocalDateTime expirationDate;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
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
}