package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.Item;
import com.itbaizhan.farm_warehousing.entity.ItemSku;
import com.itbaizhan.farm_warehousing.mapper.ItemMapper;
import com.itbaizhan.farm_warehousing.mapper.ItemSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物料Service
 */
@Service
@Transactional
public class ItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemSkuMapper itemSkuMapper;

    /**
     * 分页查询物料
     * @param page 当前页
     * @param size 每页显示条数
     * @param itemName 物料名称
     * @param itemCode 物料编码
     * @param categoryId 物料分类ID
     * @param brandId 品牌ID
     * @return 物料分页数据
     */
    public IPage<Item> findItemPage(int page, int size, String itemName, String itemCode,Long categoryId,Long brandId) {
        Page<Item> pageObj = new Page<>(page, size);
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(itemName)) {
            queryWrapper.like("item_name", itemName);
        }
        if (StringUtils.hasText(itemCode)) {
            queryWrapper.like("item_code", itemCode);
        }
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (brandId != null) {
            queryWrapper.eq("brand_id", brandId);
        }
        queryWrapper.orderByDesc("create_time");
        return itemMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询物料详情
     * @param id 物料ID
     * @return 物料详情
     */
    public Item findById(Long id) {
        return itemMapper.selectById(id);
    }

    /**
     * 新增物料
     * @param item 物料信息
     * @return true成功，false失败
     */
    public boolean addItem(Item item) {
        // 检查物料编码是否存在
        if (StringUtils.hasText(item.getItemCode())) {
            QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_code", item.getItemCode());
            Item existItem = itemMapper.selectOne(queryWrapper);
            if (existItem != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        item.setCreateBy(userName);
        item.setUpdateBy(userName);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return itemMapper.insert(item) > 0;
    }

    /**
     * 修改物料
     * @param item 物料信息
     * @return true成功，false失败
     */
    public boolean updateItem(Item item) {
        // 检查物料编码是否存在(排除自己)
        if (StringUtils.hasText(item.getItemCode())) {
            QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_code", item.getItemCode())
                    .ne("id", item.getId());
            Item existItem = itemMapper.selectOne(queryWrapper);
            if (existItem != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        item.setUpdateBy(userName);
        item.setUpdateTime(LocalDateTime.now());
        return itemMapper.updateById(item) > 0;
    }

    /**
     * 删除物料
     * @param ids 物料ID列表
     * @return true成功，false失败
     */
    public boolean deleteItem(List<Long> ids) {
        // 删除物料规格
        for (Long id : ids) {
            QueryWrapper<ItemSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_id", id);
            itemSkuMapper.delete(queryWrapper);
        }
        
        return itemMapper.deleteBatchIds(ids) > 0;
    }
}



