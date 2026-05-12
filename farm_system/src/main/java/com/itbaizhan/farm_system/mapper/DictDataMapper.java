package com.itbaizhan.farm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itbaizhan.farm_system.entity.DictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据数据访问层
 */
public interface DictDataMapper extends BaseMapper<DictData> {

    /**
     * 根据字典类型ID查询字典数据列表
     *
     * @param dictType 字典类型ID，不能为null
     * @return 字典数据列表，如果没有数据则返回空列表
     */
    List<DictData> selectByDictType(@Param("dictType") Long dictType);
}
