package com.itbaizhan.farm_plant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_plant.entity.GermplasmIntro;
import com.itbaizhan.farm_plant.mapper.GermplasmIntroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 种质介绍Service
 */
@Service
@Transactional
public class GermplasmIntroService {
    @Autowired
    private GermplasmIntroMapper germplasmIntroMapper;

    /**
     * 分页查询种质介绍
     *
     * @param page 当前页
     * @param size 每页显示条数
     * @param introName 介绍名称
     * @param germplasmId 种质ID
     * @return 种质介绍分页数据
     */
    public IPage<GermplasmIntro> findGermplasmIntroPage(int page, int size, String introName, Long germplasmId) {
        Page<GermplasmIntro> pageObj = new Page<>(page, size);
        QueryWrapper<GermplasmIntro> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(introName)) {
            queryWrapper.like("intro_name", introName);
        }
        if (germplasmId != null) {
            queryWrapper.eq("germplasm_id", germplasmId);
        }
        queryWrapper.orderByDesc("create_time");
        return germplasmIntroMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询种质介绍详情
     *
     * @param id 种质介绍ID
     * @return 种质介绍详情
     */
    public GermplasmIntro findById(Long id) {
        return germplasmIntroMapper.selectById(id);
    }

    /**
     * 新增种质介绍
     *
     * @param germplasmIntro 种质介绍信息
     * @return true成功，false失败
     */
    public boolean addGermplasmIntro(GermplasmIntro germplasmIntro) {
        if (germplasmIntro == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(germplasmIntro.getIntroName())) {
            QueryWrapper<GermplasmIntro> wrapper = new QueryWrapper<>();
            wrapper.eq("germplasm_id", germplasmIntro.getGermplasmId())
                    .eq("intro_name", germplasmIntro.getIntroName());
            GermplasmIntro existIntro = germplasmIntroMapper.selectOne(wrapper);
            if (existIntro != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        String userName = SecurityUtil.getUserName();
        germplasmIntro.setCreateBy(userName);
        germplasmIntro.setUpdateBy(userName);
        germplasmIntro.setCreateTime(LocalDateTime.now());
        germplasmIntro.setUpdateTime(LocalDateTime.now());
        return germplasmIntroMapper.insert(germplasmIntro) > 0;
    }

    /**
     * 修改种质介绍
     *
     * @param germplasmIntro 种质介绍信息
     * @return true成功，false失败
     */
    public boolean updateGermplasmIntro(GermplasmIntro germplasmIntro) {
        if (germplasmIntro == null) {
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
        if (StringUtils.hasText(germplasmIntro.getIntroName())) {
            QueryWrapper<GermplasmIntro> wrapper = new QueryWrapper<>();
            wrapper.eq("germplasm_id", germplasmIntro.getGermplasmId())
                    .eq("intro_name", germplasmIntro.getIntroName())
                    .ne("intro_id", germplasmIntro.getIntroId());
            GermplasmIntro existIntro = germplasmIntroMapper.selectOne(wrapper);
            if (existIntro != null) {
                throw new BusException(CodeEnum.PLANT_NAME_EXIST);
            }
        }
        germplasmIntro.setUpdateBy(SecurityUtil.getUserName());
        germplasmIntro.setUpdateTime(LocalDateTime.now());
        return germplasmIntroMapper.updateById(germplasmIntro) > 0;
    }

    /**
     * 删除种质介绍
     *
     * @param ids 种质介绍ID列表
     * @return true成功，false失败
     */
    public boolean deleteGermplasmIntro(List<Long> ids) {
        return germplasmIntroMapper.deleteBatchIds(ids) > 0;
    }
}

