package com.itbaizhan.farm_warehousing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_warehousing.entity.InventoryHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 库存历史记录Mapper
 */
public interface InventoryHistoryMapper extends BaseMapper<InventoryHistory> {
    
    /**
     * 分页查询库存历史（多表关联查询）
     * @param page 分页对象
     * @param warehouseId 仓库ID
     * @param areaId 库区ID
     * @param itemName 商品名称
     * @param itemCode 商品编号
     * @param skuName 规格名称
     * @param skuCode 规格编号
     * @param orderType 操作类型
     * @param orderNo 操作单号
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT ih.* FROM wh_inventory_history ih " +
            "LEFT JOIN wh_item_sku sku ON ih.sku_id = sku.id " +
            "LEFT JOIN wh_item item ON sku.item_id = item.id " +
            "WHERE 1=1 " +
            "<if test='warehouseId != null'> AND ih.warehouse_id = #{warehouseId} </if>" +
            "<if test='areaId != null'> AND ih.area_id = #{areaId} </if>" +
            "<if test='orderType != null'> AND ih.order_type = #{orderType} </if>" +
            "<if test='orderNo != null and orderNo != \"\"'> AND ih.order_no LIKE CONCAT('%', #{orderNo}, '%') </if>" +
            "<if test='itemName != null and itemName != \"\"'> AND item.item_name LIKE CONCAT('%', #{itemName}, '%') </if>" +
            "<if test='itemCode != null and itemCode != \"\"'> AND item.item_code LIKE CONCAT('%', #{itemCode}, '%') </if>" +
            "<if test='skuName != null and skuName != \"\"'> AND sku.sku_name LIKE CONCAT('%', #{skuName}, '%') </if>" +
            "<if test='skuCode != null and skuCode != \"\"'> AND sku.sku_code LIKE CONCAT('%', #{skuCode}, '%') </if>" +
            "ORDER BY ih.create_time DESC" +
            "</script>")
    IPage<InventoryHistory> selectPageWithJoin(Page<InventoryHistory> page,
                                                @Param("warehouseId") Long warehouseId,
                                                @Param("areaId") Long areaId,
                                                @Param("itemName") String itemName,
                                                @Param("itemCode") String itemCode,
                                                @Param("skuName") String skuName,
                                                @Param("skuCode") String skuCode,
                                                @Param("orderType") Integer orderType,
                                                @Param("orderNo") String orderNo);
}
