package com.itbaizhan.farm_warehousing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 往来单位实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wh_merchant")
public class Merchant {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 往来单位编码
     */
    private String merchantCode;

    /**
     * 往来单位名称
     */
    private String merchantName;

    /**
     * 往来单位类型（1-供应商 2-客户 3-供应商客户）
     */
    private Integer merchantType;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 座机号
     */
    private String tel;

    /**
     * 地址
     */
    private String address;

    /**
     * Email
     */
    private String email;

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