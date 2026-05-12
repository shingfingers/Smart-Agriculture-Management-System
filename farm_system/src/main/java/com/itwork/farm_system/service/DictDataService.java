package com.itwork.farm_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_system.entity.DictData;
import com.itwork.farm_system.mapper.DictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典数据业务逻辑服务类
 */
@Service
@Transactional
public class DictDataService {
    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 分页查询字典数据
     *
     * @param page      当前页码，从1开始
     * @param size      每页记录数，必须大于0
     * @param dictLabel 字典标签，可为空，支持模糊查询
     * @param dictType  字典类型ID，可为空，精确匹配
     * @param status    字典状态，可为空，0-正常 1-停用
     * @return 分页查询结果，包含字典数据列表和分页信息
     */
    public IPage<DictData> findDictDataPage(int page, int size, String dictLabel, Long dictType, String status) {
        Page<DictData> pageObj = new Page<>(page, size);
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dictLabel)) {
            queryWrapper.like("dict_label", dictLabel);
        }
        if (dictType != null) {
            queryWrapper.eq("dict_type", dictType);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByAsc("dict_type");
        queryWrapper.orderByAsc("dict_sort");
        return dictDataMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据ID查询字典数据详情
     *
     * @param dictCode 字典数据编码，不能为null
     * @return 字典数据详情，如果不存在则返回null
     */
    public DictData findById(Long dictCode) {
        return dictDataMapper.selectById(dictCode);
    }

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型ID，不能为null
     * @return 字典数据列表，如果没有数据则返回空列表
     */
    public List<DictData> findByDictType(Long dictType) {
        return dictDataMapper.selectByDictType(dictType);
    }

    /**
     * 新增字典数据
     *
     * @param dictData 字典数据信息，不能为null，dictLabel和dictValue不能为空
     * @return 操作结果，true-成功 false-失败
     */
    public boolean addDictData(DictData dictData) {
        dictData.setCreateTime(LocalDateTime.now());
        dictData.setStatus("0");
        if (dictData.getIsDefault() == null) {
            dictData.setIsDefault("N");
        }
        return dictDataMapper.insert(dictData) > 0;
    }

    /**
     * 修改字典数据
     *
     * @param dictData 字典数据信息，不能为null，dictCode不能为空
     * @return 操作结果，true-成功 false-失败
     */
    public boolean updateDictData(DictData dictData) {
        dictData.setUpdateTime(LocalDateTime.now());
        return dictDataMapper.updateById(dictData) > 0;
    }

    /**
     * 批量删除字典数据
     *
     * @param dictCodes 字典数据编码列表，不能为null或空集合
     * @return 操作结果，true-成功 false-失败
     * @throws IllegalArgumentException 当dictCodes为null或空集合时抛出
     */
    public boolean deleteDictData(List<Long> dictCodes) {
        return dictDataMapper.deleteBatchIds(dictCodes) > 0;
    }
}
