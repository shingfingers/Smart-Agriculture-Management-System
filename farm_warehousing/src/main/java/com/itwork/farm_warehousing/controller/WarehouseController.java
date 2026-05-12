package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_system.log.BusinessType;
import com.itwork.farm_system.log.Log;
import com.itwork.farm_warehousing.entity.Warehouse;
import com.itwork.farm_warehousing.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库Controller
 */
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    /**
     * 分页查询仓库列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param warehouseName 仓库名称，可选
     * @return 仓库分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Warehouse>> getWarehouseList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "warehouseName", required = false) String warehouseName,
            @RequestParam(value = "warehouseCode", required = false) String warehouseCode) {
        IPage<Warehouse> warehousePage = warehouseService.findWarehousePage(pageNum, pageSize, warehouseName,warehouseCode);
        return BaseResult.ok(warehousePage);
    }

    /**
     * 根据id查询仓库详情
     * @param id 仓库ID
     * @return 仓库详情
     */
    @GetMapping("/getWarehouseById")
    public BaseResult<Warehouse> findById(@RequestParam("id") Long id) {
        Warehouse warehouse = warehouseService.findById(id);
        return BaseResult.ok(warehouse);
    }

    /**
     * 新增仓库
     * @param warehouse 仓库信息
     * @return 操作结果
     */
    @PostMapping("/addWarehouse")
    @PreAuthorize("hasAuthority('/warehouse/addWarehouse')")
    @Log(title = "仓库管理", businessType = BusinessType.INSERT)
    public BaseResult<?> addWarehouse(@RequestBody Warehouse warehouse) {
        warehouseService.addWarehouse(warehouse);
        return BaseResult.ok();
    }

    /**
     * 修改仓库
     * @param warehouse 仓库信息
     * @return 操作结果
     */
    @PutMapping("/updateWarehouse")
    public BaseResult<?> updateWarehouse(@RequestBody Warehouse warehouse) {
        warehouseService.updateWarehouse(warehouse);
        return BaseResult.ok();
    }

    /**
     * 删除仓库
     * @param ids 仓库ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteWarehouse")
    public BaseResult<?> deleteWarehouse(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        warehouseService.deleteWarehouse(idList);
        return BaseResult.ok();
    }
}

