package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Api(tags = "课程类别管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/subject")
@Slf4j
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @ApiOperation("Excel课程类别的批量导入")
    @PostMapping("import")
    public Result batchImport(MultipartFile file){
        try{
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return Result.ok().message("批量导入成功");
        } catch(IOException e){
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation("课程分类嵌套列表")
    @GetMapping("nested-list")
    public Result nestedList(){

        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return Result.ok().data("items", subjectVoList);
    }

    @ApiOperation("课程分类嵌套列表（改良）")
    @GetMapping("nested-list1")
    public Result nestedList1(){

        List<SubjectVo> subjectVoList = subjectService.nestedList1();
        return Result.ok().data("items", subjectVoList);
    }
}

