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
 * 盘库单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_check_order")
public class CheckOrder {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 盘点单号
     */
    private String checkOrderNo;

    /**
     * 库存盘点单状态 -1：作废 0：未盘库 1：已盘库
     *
     */
    private Integer checkOrderStatus;

    /**
     * 盈亏数
     */
    private BigDecimal checkOrderTotal;

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

    /**
     * 盘库单详情列表
     */
    @TableField(exist = false)
    private List<CheckOrderDetail> detailList;
}