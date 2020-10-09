package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Api(tags = "课程管理")
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/admin/edu/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @ApiOperation("保存课程基本信息")
    @PostMapping("/save-course-info")
    public Result saveCourseInfo(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return Result.ok().message("保存成功").data("courseId", courseId);
    }

    @ApiOperation("根据课程id获取课程基本信息")
    @GetMapping("/course-info/{id}")
    public Result getCourseInfoById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id){

        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);

        if(null != courseInfoForm){
            return Result.ok().data("item", courseInfoForm);
        }else {
            return Result.error().message("数据不存在");
        }
    }

    @ApiOperation("更新课程基本信息")
    @PutMapping("/update-course-info")
    public Result updateCourseInfo(
            @ApiParam(value ="课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        courseService.updateCourseInfo(courseInfoForm);
        return Result.ok().message("修改成功");
    }

    @ApiOperation("分页课程列表")
    @GetMapping("/list/{page}/{limit}")
    public Result list(
            @ApiParam(value ="当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value ="每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value ="课程查询对象", required = false) CourseQuery courseQuery
    ){

        Page<CourseVo> pageModel = courseService.selectPage(page, limit, courseQuery);
        return Result.ok().data("total", pageModel.getTotal()).data("rows", pageModel.getRecords());
    }

    @ApiOperation("根据id删除课程")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@ApiParam(value = "课程id", required = true)
                             @PathVariable String id){
        //封面删除：oss
        boolean result = courseService.removeCoverById(id);
        if(!result){
            log.warn("课程封面删除失败，课程id：" + id);
        }
        //视频删除 TODO

        //课程删除
        boolean result2 = courseService.removeCourseById(id);
        if(result2){
            return Result.ok().message("删除成功");
        }else {
            return Result.error().message("删除失败");
        }
    }

    @ApiOperation("根据id获取课程发布信息")
    @GetMapping("/course-publish/{id}")
    public Result getCoursePublishVoById(@ApiParam(value = "课程id", required = true)
                                       @PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishByVoId(id);

        if(null != coursePublishVo){
            return Result.ok().data("item", coursePublishVo);
        }else {
            return Result.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id发布课程")
    @PutMapping("/publish-course/{id}")
    public Result publishCourseById(@ApiParam(value = "课程id", required = true)
                                    @PathVariable String id){
        boolean result = courseService.publishCourseById(id);

        if(result){
            return Result.ok().message("发布成功");
        }else {
            return Result.error().message("数据不存在");
        }
    }
}

