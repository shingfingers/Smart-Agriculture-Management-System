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
 * 移库单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_movement_order")
public class MovementOrder {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 移库单号
     */
    private String movementOrderNo;

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
     * 状态
     */
    private Integer movementOrderStatus;

    /**
     * 总数量
     */
    private BigDecimal totalQuantity;

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
     * 移库单详情列表
     */
    @TableField(exist = false)
    private List<MovementOrderDetail> detailList;
}