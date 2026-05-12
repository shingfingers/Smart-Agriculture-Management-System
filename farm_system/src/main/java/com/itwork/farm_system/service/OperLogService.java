package com.itwork.farm_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_system.entity.OperLog;
import com.itwork.farm_system.log.BusinessType;
import com.itwork.farm_system.mapper.OperLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 操作日志服务层
 */
@Service
public class OperLogService{
    @Autowired
    private OperLogMapper operLogMapper;

    /**
     * 记录操作日志
     */
    public void recordOperLog(OperLog operLog) {
        operLogMapper.insert(operLog);
    }

    /**
     * 分页查询操作日志
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param title 模块标题
     * @param businessType 业务类型
     * @param operName 操作人员
     * @param requestMethod 请求方式
     * @param operIp 主机地址
     * @param status 操作状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    public Page<OperLog> selectOperLogList(Integer pageNum, Integer pageSize,
                                           String title,
                                           BusinessType businessType,
                                           String operName,
                                           String requestMethod,
                                           String operIp,
                                           Integer status,
                                           LocalDateTime startTime,
                                           LocalDateTime endTime) {

        Page<OperLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OperLog> queryWrapper = new QueryWrapper<>();

        // 模块标题模糊查询
        if (StringUtils.hasText(title)) {
            queryWrapper.like("title", title);
        }

        // 业务类型精确查询
        if (businessType != null) {
            queryWrapper.eq("business_type", businessType);
        }

        // 操作人员模糊查询
        if (StringUtils.hasText(operName)) {
            queryWrapper.like("oper_name", operName);
        }

        // 请求方式精确查询
        if (StringUtils.hasText(requestMethod)) {
            queryWrapper.eq("request_method", requestMethod);
        }

        // 主机地址模糊查询
        if (StringUtils.hasText(operIp)) {
            queryWrapper.like("oper_ip", operIp);
        }

        // 操作状态精确查询
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        // 时间范围查询
        if (startTime != null) {
            queryWrapper.ge("oper_time", startTime);
        }
        if (endTime != null) {
            queryWrapper.le("oper_time", endTime);
        }

        // 按操作时间倒序排序
        queryWrapper.orderByDesc("oper_time");

        return operLogMapper.selectPage(page, queryWrapper);
    }

    /**
     * 根据ID查询操作日志
     * @param operId 操作日志ID
     * @return 操作日志对象
     */
    public OperLog selectOperLogById(Long operId) {
        return operLogMapper.selectById(operId);
    }
}

