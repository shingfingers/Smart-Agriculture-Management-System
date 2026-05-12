package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.ItemSku;
import com.itbaizhan.farm_warehousing.mapper.ItemSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物料规格Service
 */
@Service
@Transactional
public class ItemSkuService {
    @Autowired
    private ItemSkuMapper itemSkuMapper;

    /**
     * 分页查询物料规格
     * @param page 当前页
     * @param size 每页显示条数
     * @param skuName 规格名称
     * @param skuCode 规格编码
     * @param itemId 物料ID
     * @return 物料规格分页数据
     */
    public IPage<ItemSku> findItemSkuPage(int page, int size, String skuName,String skuCode,Long itemId) {
        Page<ItemSku> pageObj = new Page<>(page, size);
        QueryWrapper<ItemSku> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(skuName)) {
            queryWrapper.like("sku_name", skuName);
        }
        if (StringUtils.hasText(skuCode)) {
            queryWrapper.like("sku_code", skuCode);
        }
        if (itemId != null) {
            queryWrapper.eq("item_id", itemId);
        }
        queryWrapper.orderByDesc("create_time");
        return itemSkuMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询物料规格详情
     * @param id 物料规格ID
     * @return 物料规格详情
     */
    public ItemSku findById(Long id) {
        return itemSkuMapper.selectById(id);
    }

    /**
     * 新增物料规格
     * @param itemSku 物料规格信息
     * @return true成功，false失败
     */
    public boolean addItemSku(ItemSku itemSku) {
        // 检查SKU编码是否存在
        if (StringUtils.hasText(itemSku.getSkuCode())) {
            QueryWrapper<ItemSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sku_code", itemSku.getSkuCode());
            ItemSku existItemSku = itemSkuMapper.selectOne(queryWrapper);
            if (existItemSku != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }
        // 同一物料下检查规格名称是否存在
        if (StringUtils.hasText(itemSku.getSkuName())) {
            QueryWrapper<ItemSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_id", itemSku.getItemId())
                    .eq("sku_name", itemSku.getSkuName());
            ItemSku existItemSku = itemSkuMapper.selectOne(queryWrapper);
            if (existItemSku != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        itemSku.setCreateBy(userName);
        itemSku.setUpdateBy(userName);
        itemSku.setCreateTime(LocalDateTime.now());
        itemSku.setUpdateTime(LocalDateTime.now());
        return itemSkuMapper.insert(itemSku) > 0;
    }

    /**
     * 修改物料规格
     * @param itemSku 物料规格信息
     * @return true成功，false失败
     */
    public boolean updateItemSku(ItemSku itemSku) {
        // 检查SKU编码是否存在(排除自己)
        if (StringUtils.hasText(itemSku.getSkuCode())) {
            QueryWrapper<ItemSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sku_code", itemSku.getSkuCode())
                    .ne("id", itemSku.getId());
            ItemSku existItemSku = itemSkuMapper.selectOne(queryWrapper);
            if (existItemSku != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }

        // 同一物料下检查规格名称是否存在(排除自己)
        if (StringUtils.hasText(itemSku.getSkuName())) {
            QueryWrapper<ItemSku> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_id", itemSku.getItemId())
                    .eq("sku_name", itemSku.getSkuName())
                    .ne("id", itemSku.getId());
            ItemSku existItemSku = itemSkuMapper.selectOne(queryWrapper);
            if (existItemSku != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        itemSku.setUpdateBy(userName);
        itemSku.setUpdateTime(LocalDateTime.now());
        return itemSkuMapper.updateById(itemSku) > 0;
    }

    /**
     * 删除物料规格
     * @param ids 物料规格ID列表
     * @return true成功，false失败
     */
    public boolean deleteItemSku(List<Long> ids) {
        return itemSkuMapper.deleteBatchIds(ids) > 0;
    }
}



