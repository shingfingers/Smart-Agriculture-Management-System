package com.itbaizhan.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 雇员表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_employee")
public class Employee {

    /**
     * 雇员ID
     */
    @TableId(value = "employee_id", type = IdType.AUTO)
    private Long employeeId;

    /**
     * 编码
     */
    private String employeeCode;

    /**
     * 姓名
     */
    private String employeeName;

    /**
     * 雇员类型
     */
    private String employeeType;

    /**
     * 手机号码
     */
    private String employeeTel;

    /**
     * 雇员性别
     */
    private String employeeSex;

    /**
     * 地址
     */
    private String employeeAddress;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
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
     * 修改时间
     */
    private LocalDateTime updateTime;
}