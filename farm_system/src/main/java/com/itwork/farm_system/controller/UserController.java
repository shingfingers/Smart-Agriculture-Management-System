package com.itwork.farm_system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_system.entity.User;
import com.itwork.farm_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


//返回的都是JSON数据
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
        // 处理 HTTP GET 请求的注解,访问 这个地址时，被标记的方法就会被自动调用
    @GetMapping("/findById")
    public BaseResult<User> findById(Long id){
        User user=userService.findById(id);
        return BaseResult.ok(user);
    }

    /**
     * 根据ID查询用户详情（兼容旧接口）
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/getUserById")
    public BaseResult<User> getUserById(@RequestParam("id") Long id){
        User user = userService.findById(id);
        return BaseResult.ok(user);
    }


/*新增用户*/
    @PostMapping("/addUser")
    public  BaseResult  addUser(@RequestBody User user) {
        userService.addUser(user);
        return BaseResult.ok();
    }
    /*修改用户*/
    @PutMapping("/updateUser")
    public  BaseResult updateUser(@RequestBody User user){
        userService.updataUser(user);
        return BaseResult.ok();
    }
/*重置密码*/
    @PutMapping("/resetPassword")
    public  BaseResult resetPassword(Long userId,String newPassword){
        userService.resetPassword(userId,newPassword);
        return BaseResult.ok();
    }

    @PutMapping("/changeStatus")
    public  BaseResult updateUser(Long userId,String status){
        userService.updataStatus(userId, status);
        return BaseResult.ok();
    }
    /**
     * 删除用户
     * @param ids 用户ID字符串，多个ID用逗号分隔
     * @return 操作结果
     */
    @DeleteMapping("/deleteUser")
    public BaseResult deleteUser(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        userService.deleteUser(idList);
        return BaseResult.ok();
    }
    /**
     * 分页查询用户
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param userName 用户名，可选
     * @param status 用户状态，可选
     * @return 用户分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<User>> getUserList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "status", required = false) String status) {
        IPage<User> result = userService.findUserPage(pageNum, pageSize, userName, status);
        return BaseResult.ok(result);
    }
    /**
     * 给用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @PutMapping("/assignRoles")
    //@RequestParam从 HTTP 请求中提取参数，并绑定到方法的参数上
    public BaseResult assignRoles(@RequestParam("userId") Long userId,
                                  @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        userService.assignRoles(userId, roleIds);
        return BaseResult.ok();
    }
    /**
     * 获取登录管理员名
     *
     * @return 管理员名
     */
    @GetMapping("/getUsername")
    public BaseResult<String> getUsername() {
        String username = SecurityUtil.getUserName();
        return BaseResult.ok(username);
    }

}
