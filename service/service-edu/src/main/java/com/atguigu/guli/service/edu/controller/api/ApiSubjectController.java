package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-11 16:09
 */
@Api(tags = "课程类别")
@CrossOrigin
@RestController
@RequestMapping("/api/edu/subject")
@Slf4j
public class ApiSubjectController{
    @Autowired
    private SubjectService subjectService;

    @ApiOperation("课程分类嵌套列表")
    @GetMapping("nested-list")
    public Result nestedList(){

        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return Result.ok().data("items", subjectVoList);
    }
}
