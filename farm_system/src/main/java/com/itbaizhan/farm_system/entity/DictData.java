package com.itbaizhan.farm_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 字典数据表 sys_dict_data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict_data")
public class DictData {
    /** 字典编码 */
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    /** 字典排序 */
    private Integer dictSort;

    /** 字典标签 */
    private String dictLabel;

    /** 字典数值 */
    private String dictValue;

    /** 字典类型id */
    private Long dictType;

    /** 是否默认（Y是 N否） */
    private String isDefault;

    /** 状态（0正常 1停用） */
    private String status;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 备注 */
    private String remark;
}

