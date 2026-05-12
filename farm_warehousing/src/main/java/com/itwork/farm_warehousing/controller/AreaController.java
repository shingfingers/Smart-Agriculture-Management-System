package com.itwork.farm_warehousing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_warehousing.entity.Area;
import com.itwork.farm_warehousing.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库区Controller
 */
@RestController
@RequestMapping("/area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    /**
     * 分页查询库区列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param areaName 库区名称，可选
     * @param areaCode 库区编码，可选
     * @param warehouseId 仓库ID，可选
     * @return 库区分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Area>> getAreaList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "areaName", required = false) String areaName,
            @RequestParam(value = "areaCode", required = false) String areaCode,
            @RequestParam(value = "warehouseId", required = false) Long warehouseId) {
        IPage<Area> areaPage = areaService.findAreaPage(pageNum, pageSize, areaName,areaCode,warehouseId);
        return BaseResult.ok(areaPage);
    }

    /**
     * 根据id查询库区详情
     * @param id 库区ID
     * @return 库区详情
     */
    @GetMapping("/getAreaById")
    public BaseResult<Area> findById(@RequestParam("id") Long id) {
        Area area = areaService.findById(id);
        return BaseResult.ok(area);
    }

    /**
     * 新增库区
     * @param area 库区信息
     * @return 操作结果
     */
    @PostMapping("/addArea")
    public BaseResult<?> addArea(@RequestBody Area area) {
        areaService.addArea(area);
        return BaseResult.ok();
    }

    /**
     * 修改库区
     * @param area 库区信息
     * @return 操作结果
     */
    @PutMapping("/updateArea")
    public BaseResult<?> updateArea(@RequestBody Area area) {
        areaService.updateArea(area);
        return BaseResult.ok();
    }

    /**
     * 删除库区
     * @param ids 库区ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteArea")
    public BaseResult<?> deleteArea(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        areaService.deleteArea(idList);
        return BaseResult.ok();
    }
}

