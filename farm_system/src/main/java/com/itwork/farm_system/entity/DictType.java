package com.itwork.farm_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; /**
 * 字典类型表 sys_dict_type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict_type")
public class DictType {
    /** 字典主键 */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /** 字典名称 */
    private String dictName;

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
