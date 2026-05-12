package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.Employee;
import com.itbaizhan.farm_plant.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 雇员Service
 */
@Service
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 分页查询雇员
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param employeeName 雇员名称
     * @param employeeCode 雇员编码
     * @return 雇员分页数据
     */
    public IPage<Employee> findEmployeePage(int page, int size, String employeeName, String employeeCode,String employeeType) {
        Page<Employee> pageObj = new Page<>(page, size);
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(employeeName)) {
            queryWrapper.like("employee_name", employeeName);
        }
        if (StringUtils.hasText(employeeCode)) {
            queryWrapper.like("employee_code", employeeCode);
        }
        if(StringUtils.hasText(employeeType)){
            queryWrapper.eq("employ_type",employeeType);
        }
        queryWrapper.orderByDesc("create_time");
        return employeeMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询雇员详情
     *
     * @param id 雇员ID
     * @return 雇员详情
     */
    public Employee findById(Long id) {
        return employeeMapper.selectById(id);
    }

    /**
     * 新增雇员
     *
     * @param employee 雇员信息
     * @return true成功，false失败
     */
    public boolean addEmployee(Employee employee) {
        if (employee == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(employee.getEmployeeCode())) {
            QueryWrapper<Employee> wrapper = new QueryWrapper<>();
            wrapper.eq("employee_code", employee.getEmployeeCode());
            Employee existEmployee = employeeMapper.selectOne(wrapper);
            if (existEmployee != null) {
                throw new BusException(CodeEnum.PLANT_CODE_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        employee.setCreateBy(userName);
        employee.setUpdateBy(userName);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        return employeeMapper.insert(employee) > 0;
    }

    /**
     * 修改雇员
     *
     * @param employee 雇员信息
     * @return true成功，false失败
     */
    public boolean updateEmployee(Employee employee) {
        if (employee == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(employee.getEmployeeCode())) {
            QueryWrapper<Employee> wrapper = new QueryWrapper<>();
            wrapper.eq("employee_code", employee.getEmployeeCode())
                    .ne("employee_id", employee.getEmployeeId());
            Employee existEmployee = employeeMapper.selectOne(wrapper);
            if (existEmployee != null) {
                throw new BusException(CodeEnum.PLANT_CODE_EXIST);
            }
        }
        employee.setUpdateBy(SecurityUtil.getUserName());
        employee.setUpdateTime(LocalDateTime.now());
        return employeeMapper.updateById(employee) > 0;
    }

    /**
     * 删除雇员
     *
     * @param ids 雇员ID列表
     * @return true成功，false失败
     */
    public boolean deleteEmployee(List<Long> ids) {
        return employeeMapper.deleteBatchIds(ids) > 0;
    }
}

