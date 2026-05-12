package com.itwork.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_task")
public class Task {

    /**
     * 任务ID
     */
    @TableId(value = "task_id", type = IdType.AUTO)
    private Long taskId;

    /**
     * 批次ID
     */
    private Long batchId;

    /**
     * 任务负责人
     */
    private Long taskHead;

    /**
     * 负责人名字
     */
    private String taskHeadName;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 计划开始日期
     */
    private LocalDateTime planStart;

    /**
     * 计划结束日期
     */
    private LocalDateTime planFinish;

    /**
     * 实际开始日期
     */
    private LocalDateTime actualStart;

    /**
     * 实际结束日期
     */
    private LocalDateTime actualFinish;

    /**
     * 任务详情
     */
    private String taskDetail;

    /**
     * 图片资料
     */
    private String taskImages;

    /**
     * 视频资料
     */
    private String taskVideos;

    /**
     * 备注
     */
    private String remark;

    /**
     * 0未分配 1已分配 2进行中 3已完成
     */
    private String status;

    /**
     * 雇员列表
     */
    @TableField(exist = false)
    private List<Employee> employees;

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