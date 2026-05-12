package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.Employee;
import com.itwork.farm_plant.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 雇员Controller
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 分页查询雇员列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param employeeName 雇员名称，可选
     * @param employeeCode 雇员编码，可选
     * @param employeeType 雇员类型，可选
     * @return 雇员分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Employee>> getEmployeeList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "employeeName", required = false) String employeeName,
            @RequestParam(value = "employeeCode", required = false) String employeeCode,
            @RequestParam(value = "employeeType", required = false) String employeeType) {
        IPage<Employee> employeePage = employeeService.findEmployeePage(pageNum, pageSize, employeeName, employeeCode, employeeType);
        return BaseResult.ok(employeePage);
    }

    /**
     * 根据id查询雇员详情
     *
     * @param id 雇员ID
     * @return 雇员详情
     */
    @GetMapping("/getEmployeeById")
    public BaseResult<Employee> findById(@RequestParam("id") Long id) {
        Employee employee = employeeService.findById(id);
        return BaseResult.ok(employee);
    }

    /**
     * 新增雇员
     *
     * @param employee 雇员信息
     * @return 操作结果
     */
    @PostMapping("/addEmployee")
    public BaseResult<?> addEmployee(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return BaseResult.ok();
    }

    /**
     * 修改雇员
     *
     * @param employee 雇员信息
     * @return 操作结果
     */
    @PutMapping("/updateEmployee")
    public BaseResult<?> updateEmployee(@RequestBody Employee employee) {
        employeeService.updateEmployee(employee);
        return BaseResult.ok();
    }

    /**
     * 删除雇员
     *
     * @param ids 雇员ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteEmployee")
    public BaseResult<?> deleteEmployee(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        employeeService.deleteEmployee(idList);
        return BaseResult.ok();
    }
}

