package com.itwork.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_warehousing.entity.CheckOrderDetail;
import com.itwork.farm_warehousing.mapper.CheckOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 盘库单详情服务层
 */
@Service
@Transactional
public class CheckOrderDetailService {
    @Autowired
    private CheckOrderDetailMapper checkOrderDetailMapper;

    /**
     * 分页查询盘库单详情
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param checkOrderId 盘库单id,用于查询特定盘库单的详情
     * @return 盘库单详情列表
     */
    public IPage<CheckOrderDetail> selectCheckOrderDetailPage(Integer pageNo, Integer pageSize, Long checkOrderId){
        QueryWrapper<CheckOrderDetail> queryWrapper = new QueryWrapper<>();
        if (checkOrderId != null){
            queryWrapper.eq("check_order_id",checkOrderId);
        }
        queryWrapper.orderByDesc("create_time");
        Page<CheckOrderDetail> page = new Page<>(pageNo,pageSize);
        return checkOrderDetailMapper.selectPage(page,queryWrapper);
    }

    /**
     * 根据id查询盘库单详情
     * @param id 盘库单详情id
     * @return 盘库单详情
     */
    public CheckOrderDetail getCheckOrderDetailById(Long id){
        return checkOrderDetailMapper.selectById(id);
    }

    /**
     * 添加盘库单详情
     * @param checkOrderDetail 盘库单详情信息
     * @return 添加结果
     */
    public boolean addCheckOrderDetail(CheckOrderDetail checkOrderDetail){
        String userName = SecurityUtil.getUserName();
        checkOrderDetail.setCreateBy(userName);
        checkOrderDetail.setUpdateBy(userName);
        checkOrderDetail.setCreateTime(LocalDateTime.now());
        checkOrderDetail.setUpdateTime(LocalDateTime.now());

        return checkOrderDetailMapper.insert(checkOrderDetail) > 0;
    }

    /**
     * 修改盘库单详情
     * @param checkOrderDetail 盘库单详情信息
     * @return 修改结果
     */
    public boolean updateCheckOrderDetail(CheckOrderDetail checkOrderDetail){
        String userName = SecurityUtil.getUserName();
        checkOrderDetail.setUpdateBy(userName);
        checkOrderDetail.setUpdateTime(LocalDateTime.now());
        return checkOrderDetailMapper.updateById(checkOrderDetail) > 0;
    }

    /**
     * 批量删除盘库单详情
     * @param ids 盘库单详情id列表
     * @return 删除结果
     */
    public boolean deleteCheckOrderDetail(List<Long> ids){
        return checkOrderDetailMapper.deleteBatchIds(ids) > 0;
    }
}

