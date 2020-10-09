package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-29 16:55
 */
@ApiModel("课时信息")
@Data
public class VideoVo{
    @ApiModelProperty(value = "课时ID")
    private String id;
    @ApiModelProperty(value = "课时标题")
    private String title;
    @ApiModelProperty(value = "是否可以试听")
    private Boolean free;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "阿里云视频ID")
    private String videoSourceId;
}
