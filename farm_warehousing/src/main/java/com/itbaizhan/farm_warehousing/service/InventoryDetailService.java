package com.itbaizhan.farm_warehousing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_warehousing.entity.InventoryDetail;
import com.itbaizhan.farm_warehousing.mapper.InventoryDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * 库存详情服务
 */
@Service
@Transactional
public class InventoryDetailService {
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;

    /**
     * 分页查询库存详情
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param warehouseId 仓库ID
     * @param areaId 库区ID
     * @param orderNo 入库单号，模糊查询
     * @param itemName 商品名称，模糊查询
     * @param itemCode 商品编号，模糊查询
     * @param skuName 规格名称，模糊查询
     * @param skuCode 规格编号，模糊查询
     * @param receiptDate 入库日期
     * @return 查询结果
     */
    public IPage<InventoryDetail> selectPage(Integer pageNo, Integer pageSize,
                                             Long warehouseId, Long areaId, String orderNo,
                                             String itemName, String itemCode,
                                             String skuName, String skuCode,
                                             LocalDate receiptDate){
        Page<InventoryDetail> page = new Page<>(pageNo, pageSize);
        return inventoryDetailMapper.selectPageWithJoin(page, warehouseId, areaId, orderNo, itemName, itemCode, skuName, skuCode, receiptDate);
    }

    /**
     * 根据ID查询库存详情
     * @param id 库存详情ID
     * @return 查询结果
     */
    public InventoryDetail getInventoryDetailById(Long id){
        return inventoryDetailMapper.selectById(id);
    }
}
