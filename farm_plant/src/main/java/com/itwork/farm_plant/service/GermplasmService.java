package com.itwork.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_plant.entity.Germplasm;
import com.itwork.farm_plant.mapper.GermplasmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 种质Service
 */
@Service
@Transactional
public class GermplasmService {
    @Autowired
    private GermplasmMapper germplasmMapper;

    /**
     * 分页查询种质
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param germplasmName 种质名称
     * @param cropName 作物名称
     * @return 种质分页数据
     */
    public IPage<Germplasm> findGermplasmPage(int page, int size, String germplasmName, String cropName) {
        Page<Germplasm> pageObj = new Page<>(page, size);
        QueryWrapper<Germplasm> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(germplasmName)) {
            queryWrapper.like("germplasm_name", germplasmName);
        }
        if (StringUtils.hasText(cropName)) {
            queryWrapper.like("crop_name", cropName);
        }
        queryWrapper.orderByDesc("create_time");
        return germplasmMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询种质详情
     *
     * @param id 种质ID
     * @return 种质详情
     */
    public Germplasm findById(Long id) {
        return germplasmMapper.selectById(id);
    }

    /**
     * 新增种质
     *
     * @param germplasm 种质信息
     * @return true成功，false失败
     */
    public boolean addGermplasm(Germplasm germplasm) {
        if (germplasm == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(germplasm.getGermplasmName())) {
            QueryWrapper<Germplasm> wrapper = new QueryWrapper<>();
            wrapper.eq("germplasm_name", germplasm.getGermplasmName());
            Germplasm existGermplasm = germplasmMapper.selectOne(wrapper);
            if (existGermplasm != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        germplasm.setCreateBy(userName);
        germplasm.setUpdateBy(userName);
        germplasm.setCreateTime(LocalDateTime.now());
        germplasm.setUpdateTime(LocalDateTime.now());
        return germplasmMapper.insert(germplasm) > 0;
    }

    /**
     * 修改种质
     *
     * @param germplasm 种质信息
     * @return true成功，false失败
     */
    public boolean updateGermplasm(Germplasm germplasm) {
        if (germplasm == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(germplasm.getGermplasmName())) {
            QueryWrapper<Germplasm> wrapper = new QueryWrapper<>();
            wrapper.eq("germplasm_name", germplasm.getGermplasmName())
                    .ne("germplasm_id", germplasm.getGermplasmId());
            Germplasm existGermplasm = germplasmMapper.selectOne(wrapper);
            if (existGermplasm != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        germplasm.setUpdateBy(SecurityUtil.getUserName());
        germplasm.setUpdateTime(LocalDateTime.now());
        return germplasmMapper.updateById(germplasm) > 0;
    }

    /**
     * 删除种质
     *
     * @param ids 种质ID列表
     * @return true成功，false失败
     */
    public boolean deleteGermplasm(List<Long> ids) {
        return germplasmMapper.deleteBatchIds(ids) > 0;
    }
}

