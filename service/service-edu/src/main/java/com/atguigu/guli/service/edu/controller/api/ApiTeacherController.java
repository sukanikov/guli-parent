package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-09 18:47
 */
@CrossOrigin
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController{
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @GetMapping("/list")
    public Result listAll(){
        List<Teacher> list = teacherService.list();
        return Result.ok().data("items", list);
    }

    @ApiOperation("获取讲师基本信息和课程列表")
    @GetMapping("/get/{id}")
    public Result getInfo(
            @ApiParam(value = "讲师id", required = true)
            @PathVariable String id){
        Map<String, Object> map = teacherService.selectTeacherInfoById(id);
        return Result.ok().data(map);
    }
}
