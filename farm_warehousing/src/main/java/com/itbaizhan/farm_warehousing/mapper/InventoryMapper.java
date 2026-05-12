package com.itbaizhan.farm_warehousing.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_warehousing.entity.Inventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 库存Mapper
 */
public interface InventoryMapper extends BaseMapper<Inventory> {
    
    /**
     * 分页查询库存（多表关联查询）
     * @param page 分页对象
     * @param warehouseId 仓库ID
     * @param areaId 库区ID
     * @param itemName 商品名称
     * @param itemCode 商品编号
     * @param skuName 规格名称
     * @param skuCode 规格编号
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT inv.* FROM wh_inventory inv " +
            "LEFT JOIN wh_item_sku sku ON inv.sku_id = sku.id " +
            "LEFT JOIN wh_item item ON sku.item_id = item.id " +
            "WHERE 1=1 " +
            "<if test='warehouseId != null'> AND inv.warehouse_id = #{warehouseId} </if>" +
            "<if test='areaId != null'> AND inv.area_id = #{areaId} </if>" +
            "<if test='itemName != null and itemName != \"\"'> AND item.item_name LIKE CONCAT('%', #{itemName}, '%') </if>" +
            "<if test='itemCode != null and itemCode != \"\"'> AND item.item_code LIKE CONCAT('%', #{itemCode}, '%') </if>" +
            "<if test='skuName != null and skuName != \"\"'> AND sku.sku_name LIKE CONCAT('%', #{skuName}, '%') </if>" +
            "<if test='skuCode != null and skuCode != \"\"'> AND sku.sku_code LIKE CONCAT('%', #{skuCode}, '%') </if>" +
            "ORDER BY inv.create_time DESC" +
            "</script>")
    IPage<Inventory> selectPageWithJoin(Page<Inventory> page,
                                         @Param("warehouseId") Long warehouseId,
                                         @Param("areaId") Long areaId,
                                         @Param("itemName") String itemName,
                                         @Param("itemCode") String itemCode,
                                         @Param("skuName") String skuName,
                                         @Param("skuCode") String skuCode);
}
