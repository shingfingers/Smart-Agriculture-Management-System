package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.util.SecurityUtil;
import com.itbaizhan.farm_warehousing.entity.ReceiptOrderDetail;
import com.itbaizhan.farm_warehousing.mapper.ReceiptOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 入库单详情服务层
 */
@Service
@Transactional
public class ReceiptOrderDetailService {
    @Autowired
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;

    /**
     * 添加入库单详情
     * @param receiptOrderDetail 入库单详情信息
     * @return 添加结果
     */
    public boolean addReceiptOrderDetail(ReceiptOrderDetail receiptOrderDetail){
        String userName = SecurityUtil.getUserName();
        receiptOrderDetail.setCreateBy(userName);
        receiptOrderDetail.setUpdateBy(userName);
        receiptOrderDetail.setCreateTime(LocalDateTime.now());
        receiptOrderDetail.setUpdateTime(LocalDateTime.now());

        return receiptOrderDetailMapper.insert(receiptOrderDetail) > 0;
    }

    /**
     * 修改入库单详情
     * @param receiptOrderDetail 入库单详情信息
     * @return 修改结果
     */
    public boolean updateReceiptOrderDetail(ReceiptOrderDetail receiptOrderDetail){
        String userName = SecurityUtil.getUserName();
        receiptOrderDetail.setUpdateBy(userName);
        receiptOrderDetail.setUpdateTime(LocalDateTime.now());
        return receiptOrderDetailMapper.updateById(receiptOrderDetail) > 0;
    }

    /**
     * 批量删除入库单详情
     * @param ids 入库单详情id列表
     * @return 删除结果
     */
    public boolean deleteReceiptOrderDetail(List<Long> ids){
        return receiptOrderDetailMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 根据id查询入库单详情
     * @param id 入库单详情id
     * @return 入库单详情
     */
    public ReceiptOrderDetail getReceiptOrderDetailById(Long id){
        return receiptOrderDetailMapper.selectById(id);
    }

    /**
     * 分页查询入库单详情
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param receiptOrderId 入库单id,用于查询特定入库单的详情
     * @return 入库单详情列表
     */
    public IPage<ReceiptOrderDetail> selectReceiptOrderDetailPage(Integer pageNo, Integer pageSize, Long receiptOrderId){
        QueryWrapper<ReceiptOrderDetail> queryWrapper = new QueryWrapper<>();
        if (receiptOrderId != null){
            queryWrapper.eq("receipt_order_id",receiptOrderId);
        }
        queryWrapper.orderByDesc("create_time");
        Page<ReceiptOrderDetail> page = new Page<>(pageNo,pageSize);
        return receiptOrderDetailMapper.selectPage(page,queryWrapper);
    }
}
