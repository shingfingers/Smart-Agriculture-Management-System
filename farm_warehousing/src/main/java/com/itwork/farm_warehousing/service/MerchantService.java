package com.itwork.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_warehousing.entity.Merchant;
import com.itwork.farm_warehousing.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 往来单位Service
 */
@Service
@Transactional
public class MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 分页查询往来单位
     * @param page 当前页
     * @param size 每页显示条数
     * @param merchantName 往来单位名称
     * @param merchantCode 往来单位编码
     * @param merchantType 往来单位类型
     * @return 往来单位分页数据
     */
    public IPage<Merchant> findMerchantPage(int page, int size, String merchantName,String merchantCode, Integer merchantType) {
        Page<Merchant> pageObj = new Page<>(page, size);
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(merchantName)) {
            queryWrapper.like("merchant_name", merchantName);
        }
        if (StringUtils.hasText(merchantCode)) {
            queryWrapper.like("merchant_code", merchantCode);
        }
        if (merchantType != null) {
            queryWrapper.eq("merchant_type", merchantType);
        }
        queryWrapper.orderByDesc("create_time");
        return merchantMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 根据id查询往来单位详情
     * @param id 往来单位ID
     * @return 往来单位详情
     */
    public Merchant findById(Long id) {
        return merchantMapper.selectById(id);
    }

    /**
     * 新增往来单位
     * @param merchant 往来单位信息
     * @return true成功，false失败
     */
    public boolean addMerchant(Merchant merchant) {
        // 检查往来单位编码是否存在
        if (StringUtils.hasText(merchant.getMerchantCode())) {
            QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("merchant_code", merchant.getMerchantCode());
            Merchant existMerchant = merchantMapper.selectOne(queryWrapper);
            if (existMerchant != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }

        // 检查往来单位名称是否存在
        if (StringUtils.hasText(merchant.getMerchantCode())) {
            QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("merchant_name", merchant.getMerchantName());
            Merchant existMerchant = merchantMapper.selectOne(queryWrapper);
            if (existMerchant != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        merchant.setCreateBy(userName);
        merchant.setUpdateBy(userName);
        merchant.setCreateTime(LocalDateTime.now());
        merchant.setUpdateTime(LocalDateTime.now());
        return merchantMapper.insert(merchant) > 0;
    }

    /**
     * 修改往来单位
     * @param merchant 往来单位信息
     * @return true成功，false失败
     */
    public boolean updateMerchant(Merchant merchant) {
        // 检查往来单位编码是否存在(排除自己)
        if (StringUtils.hasText(merchant.getMerchantCode())) {
            QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("merchant_code", merchant.getMerchantCode())
                    .ne("id", merchant.getId());
            Merchant existMerchant = merchantMapper.selectOne(queryWrapper);
            if (existMerchant != null) {
                throw new BusException(CodeEnum.WAREHOUSING_CODE_EXIST);
            }
        }
        // 检查往来单位名称是否存在(排除自己)
        if (StringUtils.hasText(merchant.getMerchantCode())) {
            QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("merchant_name", merchant.getMerchantName())
                    .ne("id", merchant.getId());
            Merchant existMerchant = merchantMapper.selectOne(queryWrapper);
            if (existMerchant != null) {
                throw new BusException(CodeEnum.WAREHOUSING_NAME_EXIST);
            }
        }

        String userName = SecurityUtil.getUserName();
        merchant.setUpdateBy(userName);
        merchant.setUpdateTime(LocalDateTime.now());
        return merchantMapper.updateById(merchant) > 0;
    }

    /**
     * 删除往来单位
     * @param ids 往来单位ID列表
     * @return true成功，false失败
     */
    public boolean deleteMerchant(List<Long> ids) {
        return merchantMapper.deleteBatchIds(ids) > 0;
    }
}



