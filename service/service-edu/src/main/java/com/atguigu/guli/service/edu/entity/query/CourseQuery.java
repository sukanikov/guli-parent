package com.atguigu.guli.service.edu.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-29 01:15
 */
@Data
@ApiModel("课程查询对象")
public class CourseQuery {
    @ApiModelProperty(value = "课程标题")
    private String title;
    @ApiModelProperty(value = "讲师ID")
    private String teacherId;
    @ApiModelProperty(value = "一级分类ID")
    private String subjectParentId;
    @ApiModelProperty(value = "二级分类ID")
    private String subjectId;
}
