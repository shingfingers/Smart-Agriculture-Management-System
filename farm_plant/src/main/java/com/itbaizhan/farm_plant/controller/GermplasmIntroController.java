package com.itbaizhan.farm_plant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_plant.entity.GermplasmIntro;
import com.itbaizhan.farm_plant.service.GermplasmIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 种质介绍Controller
 */
@RestController
@RequestMapping("/germplasmIntro")
public class GermplasmIntroController {
    @Autowired
    private GermplasmIntroService germplasmIntroService;

    /**
     * 分页查询种质介绍列表
     *
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param introName 介绍名称，可选
     * @param germplasmId 种质ID，可选
     * @return 种质介绍分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<GermplasmIntro>> getGermplasmIntroList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "introName", required = false) String introName,
            @RequestParam(value = "germplasmId", required = false) Long germplasmId) {
        IPage<GermplasmIntro> page = germplasmIntroService.findGermplasmIntroPage(pageNum, pageSize, introName, germplasmId);
        return BaseResult.ok(page);
    }

    /**
     * 根据id查询种质介绍详情
     *
     * @param id 种质介绍ID
     * @return 种质介绍详情
     */
    @GetMapping("/getGermplasmIntroById")
    public BaseResult<GermplasmIntro> findById(@RequestParam("id") Long id) {
        GermplasmIntro germplasmIntro = germplasmIntroService.findById(id);
        return BaseResult.ok(germplasmIntro);
    }

    /**
     * 新增种质介绍
     *
     * @param germplasmIntro 种质介绍信息
     * @return 操作结果
     */
    @PostMapping("/addGermplasmIntro")
    public BaseResult<?> addGermplasmIntro(@RequestBody GermplasmIntro germplasmIntro) {
        germplasmIntroService.addGermplasmIntro(germplasmIntro);
        return BaseResult.ok();
    }

    /**
     * 修改种质介绍
     *
     * @param germplasmIntro 种质介绍信息
     * @return 操作结果
     */
    @PutMapping("/updateGermplasmIntro")
    public BaseResult<?> updateGermplasmIntro(@RequestBody GermplasmIntro germplasmIntro) {
        germplasmIntroService.updateGermplasmIntro(germplasmIntro);
        return BaseResult.ok();
    }

    /**
     * 删除种质介绍
     *
     * @param ids 种质介绍ID字符串，多个ID用逗号分割
     * @return 操作结果
     */
    @DeleteMapping("/deleteGermplasmIntro")
    public BaseResult<?> deleteGermplasmIntro(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        germplasmIntroService.deleteGermplasmIntro(idList);
        return BaseResult.ok();
    }
}

