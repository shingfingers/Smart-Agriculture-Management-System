package com.itbaizhan.farm_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_system.entity.LoginInfo;
import com.itbaizhan.farm_system.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统访问记录控制器
 */
@RestController
@RequestMapping("/loginInfo")
public class LoginInfoController {

    @Autowired
    private LoginInfoService loginInfoService;

    /**
     * 根据ID查询登录信息
     * @param id 访问记录ID
     * @return 登录信息
     */
    @GetMapping("/getLoginInfoById")
    public BaseResult<LoginInfo> findById(@RequestParam("id") Long id) {
        LoginInfo loginInfo = loginInfoService.findById(id);
        return BaseResult.ok(loginInfo);
    }

    /**
     * 分页查询登录信息
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param userName 用户名，可选
     * @param status 登录状态，可选
     * @param ipaddr IP地址，可选
     * @return 登录信息分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<LoginInfo>> getLoginInfoList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "ipaddr", required = false) String ipaddr) {
        IPage<LoginInfo> result = loginInfoService.findLoginInfoPage(pageNum, pageSize, userName, status, ipaddr);
        return BaseResult.ok(result);
    }
}

