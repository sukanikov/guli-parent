package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-11 14:04
 */
@Api(tags = "课程")
@CrossOrigin
@RestController
@RequestMapping("/api/edu/course")
@Slf4j
public class ApiCourseController{
    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("课程列表")
    @GetMapping("list")
    public Result list(
            @ApiParam(value = "课程列表查询对象", required = false) WebCourseQuery webCourseQuery){

        //Course也可以用CourseVo，因为在页面只显示课程标题，封面，学习人数，购买人数，是否免费
        List<Course> courseList = courseService.webSelectList(webCourseQuery);
        return Result.ok().data("courseList", courseList);
    }

    @ApiOperation("根据课程id获取课程信息")
    @GetMapping("get/{id}")
    public Result getById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id){
        //获取课程基本信息
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(id);
        List<ChapterVo> chapterVoList = chapterService.nestedListByCourseId(id);

        return Result.ok().data("course", webCourseVo).data("chapterVoList", chapterVoList);
    }
}
