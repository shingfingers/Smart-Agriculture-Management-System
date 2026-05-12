package com.itwork.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_plant.entity.Land;
import com.itwork.farm_plant.service.LandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地块Controller
 */
@RestController
@RequestMapping("/land")
public class LandController {
    @Autowired
    private LandService landService;

    /**
     * 分页查询地块列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param landName 地块名称，可选
     * @param landType 地块类型，可选
     * @return 地块分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Land>> getLandList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "landName", required = false) String landName,
            @RequestParam(value = "landType", required = false) String landType) {
        IPage<Land> landPage = landService.findLandPage(pageNum, pageSize, landName, landType);
        return BaseResult.ok(landPage);
    }

    /**
     * 根据id查询地块详情
     *
     * @param id 地块ID
     * @return 地块详情
     */
    @GetMapping("/getLandById")
    public BaseResult<Land> findById(@RequestParam("id") Long id) {
        Land land = landService.findById(id);
        return BaseResult.ok(land);
    }

    /**
     * 新增地块
     *
     * @param land 地块信息
     * @return 操作结果
     */
    @PostMapping("/addLand")
    public BaseResult<?> addLand(@RequestBody Land land) {
        landService.addLand(land);
        return BaseResult.ok();
    }

    /**
     * 修改地块
     *
     * @param land 地块信息
     * @return 操作结果
     */
    @PutMapping("/updateLand")
    public BaseResult<?> updateLand(@RequestBody Land land) {
        landService.updateLand(land);
        return BaseResult.ok();
    }

    /**
     * 删除地块
     *
     * @param ids 地块ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteLand")
    public BaseResult<?> deleteLand(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        landService.deleteLand(idList);
        return BaseResult.ok();
    }
}

