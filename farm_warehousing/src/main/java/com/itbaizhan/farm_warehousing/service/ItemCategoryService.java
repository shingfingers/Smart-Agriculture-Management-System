package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.Item;
import com.itbaizhan.farm_warehousing.entity.ItemCategory;
import com.itbaizhan.farm_warehousing.mapper.ItemCategoryMapper;
import com.itbaizhan.farm_warehousing.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物料类型Service
 */
@Service
@Transactional
public class ItemCategoryService {
    @Autowired
    private ItemCategoryMapper itemCategoryMapper;
    @Autowired
    private ItemMapper itemMapper;

    /**
     * 分页查询物料类型
     * @param page 当前页
     * @param size 每页显示条数
     * @param categoryName 物料类型名称
     * @param parentId 父类型ID
     * @return 物料类型分页数据
     */
    public IPage<ItemCategory> findItemCategoryPage(int page, int size, String categoryName,Long parentId) {
        Page<ItemCategory> pageObj = new Page<>(page, size);
        QueryWrapper<ItemCategory> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(categoryName)) {
            queryWrapper.like("category_name", categoryName);
        }
        if (parentId != null) {
            queryWrapper.eq("parent_id", parentId);
        }
        queryWrapper.orderByAsc("order_num");
        return itemCategoryMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询物料类型详情
     * @param id 物料类型ID
     * @return 物料类型详情
     */
    public ItemCategory findById(Long id) {
        return itemCategoryMapper.selectById(id);
    }

    /**
     * 根据父类型ID查询物料类型
     * @param parentId 父类型ID
     * @return 物料类型列表
     */
    public List<ItemCategory> findByParentId(Long parentId) {
        QueryWrapper<ItemCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.orderByAsc("order_num");
        return itemCategoryMapper.selectList(queryWrapper);
    }

    /**
     * 新增物料类型
     * @param itemCategory 物料类型信息
     * @return true成功，false失败
     */
    public boolean addItemCategory(ItemCategory itemCategory) {
        // 检查类型名称是否存在（同一父级下）
        if (StringUtils.hasText(itemCategory.getCategoryName())) {
            QueryWrapper<ItemCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", itemCategory.getParentId())
                    .eq("category_name", itemCategory.getCategoryName());
            ItemCategory existItemCategory = itemCategoryMapper.selectOne(queryWrapper);
            if (existItemCategory != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }
        // 如果没有父类型，则设置父类型ID为0
        if (itemCategory.getParentId() == null){
            itemCategory.setParentId(0L);
        }

        String userName = SecurityUtil.getUserName();
        itemCategory.setCreateBy(userName);
        itemCategory.setUpdateBy(userName);
        itemCategory.setCreateTime(LocalDateTime.now());
        itemCategory.setUpdateTime(LocalDateTime.now());
        return itemCategoryMapper.insert(itemCategory) > 0;
    }

    /**
     * 修改物料类型
     * @param itemCategory 物料类型信息
     * @return true成功，false失败
     */
    public boolean updateItemCategory(ItemCategory itemCategory) {
        // 检查类型名称是否存在（同一父级下，排除自己）
        if (StringUtils.hasText(itemCategory.getCategoryName())) {
            QueryWrapper<ItemCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", itemCategory.getParentId())
                    .eq("category_name", itemCategory.getCategoryName())
                    .ne("id", itemCategory.getId());
            ItemCategory existItemCategory = itemCategoryMapper.selectOne(queryWrapper);
            if (existItemCategory != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }
        // 如果没有父类型，则设置父类型ID为0
        if (itemCategory.getParentId() == null){
            itemCategory.setParentId(0L);
        }
        String userName = SecurityUtil.getUserName();
        itemCategory.setUpdateBy(userName);
        itemCategory.setUpdateTime(LocalDateTime.now());
        return itemCategoryMapper.updateById(itemCategory) > 0;
    }

    /**
     * 删除物料类型，删除物料时，要递归删除其所有的子类型
     * 且每次删除时都要检查该类型下是否有物料，如果有物料，则不允许删除
     * 
     * @param ids 物料类型ID列表
     * @return true成功，false失败
     */
    public boolean deleteItemCategory(List<Long> ids) {
        // 删除物料类型
        for (Long id : ids) {
            deleteItemCategory(id);
        }
        return true;
    }

    /**
     * 递归删除该物料类型及其子物料类型
     * @param id 物料类型ID
     */
    private void deleteItemCategory(Long id) {
        // 检查该类型下是否有物料
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", id);
        Long itemCount = itemMapper.selectCount(queryWrapper);
        if (itemCount > 0){
            throw new BusException(CodeEnum.WAREHOUSING_CATEGORY_HAS_ITEM);
        }else {
            itemCategoryMapper.deleteById(id);
        }
        // 查询该类型的子类型
        List<ItemCategory> childCategories = findByParentId(id);
        if (childCategories == null || childCategories.isEmpty()) {
            return;
        }
        // 递归删除子类型
        for (ItemCategory childCategory : childCategories){
            deleteItemCategory(childCategory.getId());
        }
    }
}



