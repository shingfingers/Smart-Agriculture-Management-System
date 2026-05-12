package com.itwork.farm_warehousing.entity;

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
 * 入库单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_receipt_order")
public class ReceiptOrder {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 入库单号
     */
    private String receiptOrderNo;

    /**
     * 入库类型(1.普通入库 2.盘库入库)
     */
    private Integer receiptOrderType;

    /**
     * 供应商
     */
    private Long merchantId;

    /**
     * 商品总数
     */
    private BigDecimal totalQuantity;

    /**
     * 订单金额
     */
    private BigDecimal payableAmount;

    /**
     * 入库状态
     */
    private Integer receiptOrderStatus;

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
     * 入库单详情列表
     */
    @TableField(exist = false)
    private List<ReceiptOrderDetail> detailList;
}