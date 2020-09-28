package com.atguigu.guli.service.edu.controller.admin;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@CrossOrigin()  //开启跨域访问
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@Slf4j
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private OssFileService ossFileService;

    @DeleteMapping("/test")
    public Result removeFile(@RequestBody String url){
        ossFileService.removeFile(url);
        return Result.ok();
    };


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

    @ApiOperation(value = "根据id删除讲师", notes = "逻辑删除讲师数据，包括讲师头像")
    @DeleteMapping("remove/{id}")
    public Result removeById(
            @ApiParam(value = "讲师id", required = true)
            @PathVariable String id){
        //删除头像
        boolean isDeleteAvatar = teacherService.removeAvatarById(id);
        if(!isDeleteAvatar){
            log.warn("删除讲师头像失败，讲师id：" + id);
        }

        //删除数据
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
            @RequestBody Teacher teacher){
        boolean b = teacherService.updateById(teacher);
        if(b){
            return Result.ok().message("数据修改成功");
        }else{
            return Result.error().message("数据修改失败");
        }
    }

    @ApiOperation("根据id列表删除讲师")
    @DeleteMapping("batch-remove")
    public Result removeRows(
            @ApiParam(value = "讲师id列表", required = true)
            @RequestBody List<String> idList){
        boolean result = teacherService.removeByIds(idList);
        if(result){
            return Result.ok().message("删除成功");
        }else {
            return Result.error().message("数据不存在");
        }

    }

    @ApiOperation(value = "根据关键字查询讲师姓名列表")
    @GetMapping("list/name")
    public Result selectNameListByKey(
            @ApiParam(value = "查询关键字", required = false)
            @RequestParam(required = false) String key){
        List<Map<String, Object>> nameList = teacherService.selectNameListByKey(key);
        return Result.ok().data("nameList", nameList);
    }
}

