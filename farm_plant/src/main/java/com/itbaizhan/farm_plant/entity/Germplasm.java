package com.itbaizhan.farm_plant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 种质表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("plant_germplasm")
public class Germplasm {

    /**
     * 种质ID
     */
    @TableId(value = "germplasm_id", type = IdType.AUTO)
    private Long germplasmId;

    /**
     * 作物名称
     */
    private String cropName;

    /**
     * 作物英文名称
     */
    private String cropEnName;

    /**
     * 种质名称
     */
    private String germplasmName;

    /**
     * 种质英文名称
     */
    private String germplasmEnName;

    /**
     * 种质图片
     */
    private String germplasmImg;

    /**
     * 宣传语
     */
    private String germplasmDes;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}