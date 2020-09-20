package com.atguigu.guli.service.edu.controller.admin;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.query.TeacherQuery;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @GetMapping("/list")
    public Result listAll(){
        List<Teacher> list = teacherService.list();
        return Result.ok().data("items", list).message("获取讲师列表成功");
    }

    /**
     * 分页无条件查询
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation("分页讲师列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value ="每页记录数", required = true)
            @PathVariable Long limit){
        Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.page(pageParam);

        return Result.ok().data("pageParam", pageParam);
    }

    @ApiOperation("新增讲师")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "讲师对象", required = true)
            @RequestBody Teacher teacher){
        boolean result = teacherService.save(teacher);
        if(result){
            return Result.ok().message("添加成功");
        }else {
            return Result.error().message("添加失败");
        }
    }

    @ApiOperation(value = "根据id删除讲师", notes = "逻辑删除讲师数据")
    @DeleteMapping("remove/{id}")
    public Result removeById(
            @ApiParam(value = "讲师id", required = true)
            @PathVariable String id){
        boolean b = teacherService.removeById(id);
        if(b){
            return Result.ok().message("删除成功");
        }else{
            return Result.error().message("删除失败");
        }
    }

    @ApiOperation(value = "根据id获取讲师信息")
    @GetMapping("get/{id}")
    public Result getById(
            @ApiParam(value = "讲师id", required = true)
            @PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        if(teacher != null){
            return Result.ok().data("item", teacher);
        }else{
            return Result.error().message("数据不存在");
        }
    }

    /**
     * 分页有条件查询
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "获取分页查询讲师列表")
    @GetMapping("list-with-query/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "讲师查询对象", required = false) TeacherQuery teacherQuery){

        Page<Teacher> pageParam = new Page<>(page, limit);
        Page<Teacher> pageModel = teacherService.selectPage(pageParam, teacherQuery);
        return Result.ok().data("pageModel", pageModel);
    }

    @ApiOperation(value = "根据id更新讲师信息")
    @PutMapping("update")
    public Result updateById(
            @ApiParam(value = "讲师对象", required = true)
            @RequestBody Teacher teacher
    ){
        boolean b = teacherService.updateById(teacher);
        if(b){
            return Result.ok().message("数据修改成功");
        }else{
            return Result.error().message("数据修改失败");
        }
    }
}

