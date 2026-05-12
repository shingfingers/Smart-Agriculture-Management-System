package com.itbaizhan.farm_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_system.entity.DictType;
import com.itbaizhan.farm_system.mapper.DictTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典类型业务逻辑服务类
 */
@Service
@Transactional
public class DictTypeService {
    @Autowired
    private DictTypeMapper dictTypeMapper;

    /**
     * 分页查询字典类型
     *
     * @param page     当前页码，从1开始
     * @param size     每页记录数，必须大于0
     * @param dictName 字典名称，可为空，支持模糊查询
     * @param status   字典状态，可为空，0-正常 1-停用
     * @return 分页查询结果，包含字典类型列表和分页信息
     */
    public IPage<DictType> findDictTypePage(int page, int size, String dictName, String status) {
        Page<DictType> pageObj = new Page<>(page, size);
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dictName)) {
            queryWrapper.like("dict_name", dictName);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        return dictTypeMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据ID查询字典类型详情
     *
     * @param dictId 字典类型ID，不能为null
     * @return 字典类型详情，如果不存在则返回null
     */
    public DictType findById(Long dictId) {
        return dictTypeMapper.selectById(dictId);
    }

    /**
     * 新增字典类型
     *
     * @param dictType 字典类型信息，不能为null，dictName不能为空
     * @return 操作结果，true-成功 false-失败
     */
    public boolean addDictType(DictType dictType) {
        dictType.setCreateTime(LocalDateTime.now());
        dictType.setStatus("0");
        return dictTypeMapper.insert(dictType) > 0;
    }

    /**
     * 修改字典类型
     *
     * @param dictType 字典类型信息，不能为null，dictId不能为空
     * @return 操作结果，true-成功 false-失败
     */
    public boolean updateDictType(DictType dictType) {
        dictType.setUpdateTime(LocalDateTime.now());
        return dictTypeMapper.updateById(dictType) > 0;
    }

    /**
     * 批量删除字典类型
     *
     * @param dictIds 字典类型ID列表，不能为null或空集合
     * @return 操作结果，true-成功 false-失败
     */
    public boolean deleteDictType(List<Long> dictIds) {
        return dictTypeMapper.deleteBatchIds(dictIds) > 0;
    }

    /**
     * 查询所有有效的字典类型
     *
     * @return 有效字典类型列表，如果没有数据则返回空列表
     */
    public List<DictType> findAll() {
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");
        return dictTypeMapper.selectList(queryWrapper);
    }
}
