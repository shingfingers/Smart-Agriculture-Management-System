package com.itwork.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itwork.farm_common.util.SecurityUtil;
import com.itwork.farm_warehousing.entity.MovementOrderDetail;
import com.itwork.farm_warehousing.mapper.MovementOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 移库单详情服务层
 */
@Service
@Transactional
public class MovementOrderDetailService {
    @Autowired
    private MovementOrderDetailMapper movementOrderDetailMapper;

    /**
     * 分页查询移库单详情
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param movementOrderId 移库单id,用于查询特定移库单的详情
     * @return 移库单详情列表
     */
    public IPage<MovementOrderDetail> selectMovementOrderDetailPage(Integer pageNo, Integer pageSize, Long movementOrderId){
        QueryWrapper<MovementOrderDetail> queryWrapper = new QueryWrapper<>();
        if (movementOrderId != null){
            queryWrapper.eq("movement_order_id",movementOrderId);
        }
        queryWrapper.orderByDesc("create_time");
        Page<MovementOrderDetail> page = new Page<>(pageNo,pageSize);
        return movementOrderDetailMapper.selectPage(page,queryWrapper);
    }

    /**
     * 根据id查询移库单详情
     * @param id 移库单详情id
     * @return 移库单详情
     */
    public MovementOrderDetail getMovementOrderDetailById(Long id){
        return movementOrderDetailMapper.selectById(id);
    }

    /**
     * 添加移库单详情
     * @param movementOrderDetail 移库单详情信息
     * @return 添加结果
     */
    public boolean addMovementOrderDetail(MovementOrderDetail movementOrderDetail){
        String userName = SecurityUtil.getUserName();
        movementOrderDetail.setCreateBy(userName);
        movementOrderDetail.setUpdateBy(userName);
        movementOrderDetail.setCreateTime(LocalDateTime.now());
        movementOrderDetail.setUpdateTime(LocalDateTime.now());

        return movementOrderDetailMapper.insert(movementOrderDetail) > 0;
    }

    /**
     * 修改移库单详情
     * @param movementOrderDetail 移库单详情信息
     * @return 修改结果
     */
    public boolean updateMovementOrderDetail(MovementOrderDetail movementOrderDetail){
        String userName = SecurityUtil.getUserName();
        movementOrderDetail.setUpdateBy(userName);
        movementOrderDetail.setUpdateTime(LocalDateTime.now());
        return movementOrderDetailMapper.updateById(movementOrderDetail) > 0;
    }

    /**
     * 批量删除移库单详情
     * @param ids 移库单详情id列表
     * @return 删除结果
     */
    public boolean deleteMovementOrderDetail(List<Long> ids){
        return movementOrderDetailMapper.deleteBatchIds(ids) > 0;
    }
}

