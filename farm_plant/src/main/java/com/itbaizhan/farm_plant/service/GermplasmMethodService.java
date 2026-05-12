package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.GermplasmMethod;
import com.itbaizhan.farm_plant.mapper.GermplasmMethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 种植方法Service
 */
@Service
@Transactional
public class GermplasmMethodService {
    @Autowired
    private GermplasmMethodMapper germplasmMethodMapper;

    /**
     * 分页查询种植方法
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param methodName 方法名称
     * @param germplasmId 种质ID
     * @return 种植方法分页数据
     */
    public IPage<GermplasmMethod> findGermplasmMethodPage(int page, int size, String methodName, Long germplasmId) {
        Page<GermplasmMethod> pageObj = new Page<>(page, size);
        QueryWrapper<GermplasmMethod> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(methodName)) {
            queryWrapper.like("method_name", methodName);
        }
        if (germplasmId != null) {
            queryWrapper.eq("germplasm_id", germplasmId);
        }
        queryWrapper.orderByDesc("create_time");
        return germplasmMethodMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询种植方法详情
     *
     * @param id 种植方法ID
     * @return 种植方法详情
     */
    public GermplasmMethod findById(Long id) {
        return germplasmMethodMapper.selectById(id);
    }

    /**
     * 新增种植方法
     *
     * @param germplasmMethod 种植方法信息
     * @return true成功，false失败
     */
    public boolean addGermplasmMethod(GermplasmMethod germplasmMethod) {
        if (germplasmMethod == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(germplasmMethod.getMethodName())) {
            QueryWrapper<GermplasmMethod> wrapper = new QueryWrapper<>();
            wrapper.eq("germplasm_id", germplasmMethod.getGermplasmId())
                    .eq("method_name", germplasmMethod.getMethodName());
            GermplasmMethod existMethod = germplasmMethodMapper.selectOne(wrapper);
            if (existMethod != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        germplasmMethod.setCreateBy(userName);
        germplasmMethod.setUpdateBy(userName);
        germplasmMethod.setCreateTime(LocalDateTime.now());
        germplasmMethod.setUpdateTime(LocalDateTime.now());
        return germplasmMethodMapper.insert(germplasmMethod) > 0;
    }

    /**
     * 修改种植方法
     *
     * @param germplasmMethod 种植方法信息
     * @return true成功，false失败
     */
    public boolean updateGermplasmMethod(GermplasmMethod germplasmMethod) {
        if (germplasmMethod == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(germplasmMethod.getMethodName())) {
            QueryWrapper<GermplasmMethod> wrapper = new QueryWrapper<>();
            wrapper.eq("germplasm_id", germplasmMethod.getGermplasmId())
                    .eq("method_name", germplasmMethod.getMethodName())
                    .ne("method_id", germplasmMethod.getMethodId());
            GermplasmMethod existMethod = germplasmMethodMapper.selectOne(wrapper);
            if (existMethod != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        germplasmMethod.setUpdateBy(SecurityUtil.getUserName());
        germplasmMethod.setUpdateTime(LocalDateTime.now());
        return germplasmMethodMapper.updateById(germplasmMethod) > 0;
    }

    /**
     * 删除种植方法
     *
     * @param ids 种植方法ID列表
     * @return true成功，false失败
     */
    public boolean deleteGermplasmMethod(List<Long> ids) {
        return germplasmMethodMapper.deleteBatchIds(ids) > 0;
    }
}

