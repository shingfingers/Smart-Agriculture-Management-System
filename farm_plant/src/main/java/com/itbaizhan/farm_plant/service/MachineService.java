package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.Machine;
import com.itbaizhan.farm_plant.mapper.MachineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 农机信息Service
 */
@Service
@Transactional
public class MachineService {
    @Autowired
    private MachineMapper machineMapper;

    /**
     * 分页查询农机信息
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param machineName 农机名称
     * @param machineTypeId 农机类别ID
     * @return 农机信息分页数据
     */
    public IPage<Machine> findMachinePage(int page, int size, String machineName, Long machineTypeId) {
        Page<Machine> pageObj = new Page<>(page, size);
        QueryWrapper<Machine> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(machineName)) {
            queryWrapper.like("machine_name", machineName);
        }
        if (machineTypeId != null) {
            queryWrapper.eq("machine_type_id", machineTypeId);
        }
        queryWrapper.orderByDesc("create_time");
        return machineMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询农机信息详情
     *
     * @param id 农机信息ID
     * @return 农机信息详情
     */
    public Machine findById(Long id) {
        return machineMapper.selectById(id);
    }

    /**
     * 新增农机信息
     *
     * @param machine 农机信息
     * @return true成功，false失败
     */
    public boolean addMachine(Machine machine) {
        if (machine == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(machine.getMachineCode())) {
            QueryWrapper<Machine> wrapper = new QueryWrapper<>();
            wrapper.eq("machine_code", machine.getMachineCode());
            Machine existMachine = machineMapper.selectOne(wrapper);
            if (existMachine != null) {
                throw new BusException(CodeEnum.PLANT_CODE_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        machine.setCreateBy(userName);
        machine.setUpdateBy(userName);
        machine.setCreateTime(LocalDateTime.now());
        machine.setUpdateTime(LocalDateTime.now());
        return machineMapper.insert(machine) > 0;
    }

    /**
     * 修改农机信息
     *
     * @param machine 农机信息
     * @return true成功，false失败
     */
    public boolean updateMachine(Machine machine) {
        if (machine == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(machine.getMachineCode())) {
            QueryWrapper<Machine> wrapper = new QueryWrapper<>();
            wrapper.eq("machine_code", machine.getMachineCode())
                    .ne("machine_id", machine.getMachineId());
            Machine existMachine = machineMapper.selectOne(wrapper);
            if (existMachine != null) {
                throw new BusException(CodeEnum.PLANT_CODE_EXIST);
            }
        }
        machine.setUpdateBy(SecurityUtil.getUserName());
        machine.setUpdateTime(LocalDateTime.now());
        return machineMapper.updateById(machine) > 0;
    }

    /**
     * 删除农机信息
     *
     * @param ids 农机信息ID列表
     * @return true成功，false失败
     */
    public boolean deleteMachine(List<Long> ids) {
        return machineMapper.deleteBatchIds(ids) > 0;
    }
}

