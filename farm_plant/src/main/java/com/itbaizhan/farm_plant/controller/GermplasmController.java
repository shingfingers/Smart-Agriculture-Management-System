package com.itbaizhan.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_plant.entity.Germplasm;
import com.itbaizhan.farm_plant.service.GermplasmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 种质Controller
 */
@RestController
@RequestMapping("/germplasm")
public class GermplasmController {
    @Autowired
    private GermplasmService germplasmService;

    /**
     * 分页查询种质列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param germplasmName 种质名称，可选
     * @param cropName 作物名称，可选
     * @return 种质分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Germplasm>> getGermplasmList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "germplasmName", required = false) String germplasmName,
            @RequestParam(value = "cropName", required = false) String cropName) {
        IPage<Germplasm> germplasmPage = germplasmService.findGermplasmPage(pageNum, pageSize, germplasmName, cropName);
        return BaseResult.ok(germplasmPage);
    }

    /**
     * 根据id查询种质详情
     *
     * @param id 种质ID
     * @return 种质详情
     */
    @GetMapping("/getGermplasmById")
    public BaseResult<Germplasm> findById(@RequestParam("id") Long id) {
        Germplasm germplasm = germplasmService.findById(id);
        return BaseResult.ok(germplasm);
    }

    /**
     * 新增种质
     *
     * @param germplasm 种质信息
     * @return 操作结果
     */
    @PostMapping("/addGermplasm")
    public BaseResult<?> addGermplasm(@RequestBody Germplasm germplasm) {
        germplasmService.addGermplasm(germplasm);
        return BaseResult.ok();
    }

    /**
     * 修改种质
     *
     * @param germplasm 种质信息
     * @return 操作结果
     */
    @PutMapping("/updateGermplasm")
    public BaseResult<?> updateGermplasm(@RequestBody Germplasm germplasm) {
        germplasmService.updateGermplasm(germplasm);
        return BaseResult.ok();
    }

    /**
     * 删除种质
     *
     * @param ids 种质ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteGermplasm")
    public BaseResult<?> deleteGermplasm(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        germplasmService.deleteGermplasm(idList);
        return BaseResult.ok();
    }
}

