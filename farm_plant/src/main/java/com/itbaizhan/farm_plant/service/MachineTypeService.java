package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.MachineType;
import com.itbaizhan.farm_plant.mapper.MachineTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 农机类别Service
 */
@Service
@Transactional
public class MachineTypeService {
    @Autowired
    private MachineTypeMapper machineTypeMapper;

    /**
     * 分页查询农机类别
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param machineTypeName 农机类别名称
     * @return 农机类别分页数据
     */
    public IPage<MachineType> findMachineTypePage(int page, int size, String machineTypeName) {
        Page<MachineType> pageObj = new Page<>(page, size);
        QueryWrapper<MachineType> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(machineTypeName)) {
            queryWrapper.like("machine_type_name", machineTypeName);
        }
        queryWrapper.orderByDesc("create_time");
        return machineTypeMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询农机类别详情
     *
     * @param id 农机类别ID
     * @return 农机类别详情
     */
    public MachineType findById(Long id) {
        return machineTypeMapper.selectById(id);
    }

    /**
     * 新增农机类别
     *
     * @param machineType 农机类别信息
     * @return true成功，false失败
     */
    public boolean addMachineType(MachineType machineType) {
        if (machineType == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(machineType.getMachineTypeName())) {
            QueryWrapper<MachineType> wrapper = new QueryWrapper<>();
            wrapper.eq("machine_type_name", machineType.getMachineTypeName());
            MachineType existType = machineTypeMapper.selectOne(wrapper);
            if (existType != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        machineType.setCreateBy(userName);
        machineType.setUpdateBy(userName);
        machineType.setCreateTime(LocalDateTime.now());
        machineType.setUpdateTime(LocalDateTime.now());
        return machineTypeMapper.insert(machineType) > 0;
    }

    /**
     * 修改农机类别
     *
     * @param machineType 农机类别信息
     * @return true成功，false失败
     */
    public boolean updateMachineType(MachineType machineType) {
        if (machineType == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(machineType.getMachineTypeName())) {
            QueryWrapper<MachineType> wrapper = new QueryWrapper<>();
            wrapper.eq("machine_type_name", machineType.getMachineTypeName())
                    .ne("machine_type_id", machineType.getMachineTypeId());
            MachineType existType = machineTypeMapper.selectOne(wrapper);
            if (existType != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        machineType.setUpdateBy(SecurityUtil.getUserName());
        machineType.setUpdateTime(LocalDateTime.now());
        return machineTypeMapper.updateById(machineType) > 0;
    }

    /**
     * 删除农机类别
     *
     * @param ids 农机类别ID列表
     * @return true成功，false失败
     */
    public boolean deleteMachineType(List<Long> ids) {
        return machineTypeMapper.deleteBatchIds(ids) > 0;
    }
}

