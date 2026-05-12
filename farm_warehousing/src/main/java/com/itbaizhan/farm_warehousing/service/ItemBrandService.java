package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.exception.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.ItemBrand;
import com.itbaizhan.farm_warehousing.mapper.ItemBrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品品牌Service
 */
@Service
@Transactional
public class ItemBrandService {
    @Autowired
    private ItemBrandMapper itemBrandMapper;

    /**
     * 分页查询商品品牌
     * @param page 当前页
     * @param size 每页显示条数
     * @param brandName 品牌名称
     * @return 商品品牌分页数据
     */
    public IPage<ItemBrand> findItemBrandPage(int page, int size, String brandName) {
        Page<ItemBrand> pageObj = new Page<>(page, size);
        QueryWrapper<ItemBrand> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(brandName)) {
            queryWrapper.like("brand_name", brandName);
        }
        queryWrapper.orderByDesc("create_time");
        return itemBrandMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询商品品牌详情
     * @param id 商品品牌ID
     * @return 商品品牌详情
     */
    public ItemBrand findById(Long id) {
        return itemBrandMapper.selectById(id);
    }

    /**
     * 新增商品品牌
     * @param itemBrand 商品品牌信息
     * @return true成功，false失败
     */
    public boolean addItemBrand(ItemBrand itemBrand) {
        // 检查品牌名称是否存在
        if (StringUtils.hasText(itemBrand.getBrandName())) {
            QueryWrapper<ItemBrand> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("brand_name", itemBrand.getBrandName());
            ItemBrand existItemBrand = itemBrandMapper.selectOne(queryWrapper);
            if (existItemBrand != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        itemBrand.setCreateBy(userName);
        itemBrand.setUpdateBy(userName);
        itemBrand.setCreateTime(LocalDateTime.now());
        itemBrand.setUpdateTime(LocalDateTime.now());
        return itemBrandMapper.insert(itemBrand) > 0;
    }

    /**
     * 修改商品品牌
     * @param itemBrand 商品品牌信息
     * @return true成功，false失败
     */
    public boolean updateItemBrand(ItemBrand itemBrand) {
        // 检查品牌名称是否存在(排除自己)
        if (StringUtils.hasText(itemBrand.getBrandName())) {
            QueryWrapper<ItemBrand> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("brand_name", itemBrand.getBrandName())
                    .ne("id", itemBrand.getId());
            ItemBrand existItemBrand = itemBrandMapper.selectOne(queryWrapper);
            if (existItemBrand != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        itemBrand.setUpdateBy(userName);
        itemBrand.setUpdateTime(LocalDateTime.now());
        return itemBrandMapper.updateById(itemBrand) > 0;
    }

    /**
     * 删除商品品牌
     * @param ids 商品品牌ID列表
     * @return true成功，false失败
     */
    public boolean deleteItemBrand(List<Long> ids) {
        return itemBrandMapper.deleteBatchIds(ids) > 0;
    }
}



