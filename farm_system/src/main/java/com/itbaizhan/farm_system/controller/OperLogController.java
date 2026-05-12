package com.itbaizhan.farm_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_system.entity.OperLog;
import com.itbaizhan.farm_system.log.BusinessType;
import com.itbaizhan.farm_system.service.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 操作日志信息
 */
@RestController
@RequestMapping("/operlog")
public class OperLogController {

    @Autowired
    private OperLogService operLogService;

    /**
     * 分页查询操作日志列表
     * @param pageNum 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param title 模块标题（可选）
     * @param businessType 业务类型（可选）
     * @param operName 操作人员（可选）
     * @param requestMethod 请求方式（可选）
     * @param operIp 主机地址（可选）
     * @param status 操作状态（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 分页结果
     */
    @GetMapping("/list")
    public BaseResult<Page<OperLog>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "businessType", required = false) BusinessType businessType,
            @RequestParam(value = "operName", required = false) String operName,
            @RequestParam(value = "requestMethod", required = false) String requestMethod,
            @RequestParam(value = "operIp", required = false) String operIp,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        Page<OperLog> page = operLogService.selectOperLogList(pageNum, pageSize, title,
                businessType, operName, requestMethod, operIp, status, startTime, endTime);
        return BaseResult.ok(page);
    }

    /**
     * 根据操作日志ID查询详细信息
     * @param id 操作日志ID
     * @return 操作日志详细信息
     */
    @GetMapping("/getOperLogById")
    public BaseResult<OperLog> findById(@RequestParam("id") Long id) {
        OperLog operLog = operLogService.selectOperLogById(id);
        return BaseResult.ok(operLog);
    }
}
