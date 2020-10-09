package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Api(tags = "章节管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/chapter")
@Slf4j
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("新增章节")
    @PostMapping("/save")
    public Result save(@ApiParam(value = "章节对象")
                       @RequestBody Chapter chapter){
        boolean result = chapterService.save(chapter);

        if(result){
            return Result.ok().message("保存成功");
        }else {
            return Result.error().message("保存失败");
        }
    }

    @ApiOperation("根据id查询章节")
    @GetMapping("get/{id}")
    public Result getById(
            @ApiParam(value="章节id", required = true)
            @PathVariable String id){

        Chapter chapter = chapterService.getById(id);
        if (chapter != null) {
            return Result.ok().data("item", chapter);
        } else {
            return Result.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id修改章节")
    @PutMapping("update")
    public Result updateById(
            @ApiParam(value="章节对象", required = true)
            @RequestBody Chapter chapter){

        boolean result = chapterService.updateById(chapter);
        if (result) {
            return Result.ok().message("修改成功");
        } else {
            return Result.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id删除章节")
    @DeleteMapping("remove/{id}")
    public Result removeById(
            @ApiParam(value = "章节ID", required = true)
            @PathVariable String id){

        //TODO 删除视频：VOD
        //在此处调用vod中的删除视频文件的接口

        boolean result = chapterService.removeChapterById(id);
        if (result) {
            return Result.ok().message("删除成功");
        } else {
            return Result.error().message("数据不存在");
        }
    }

    @ApiOperation("嵌套章节数据列表")
    @GetMapping("nested-list/{courseId}")
    public Result nestedListByCourseId(@ApiParam(value = "课程id", required = true)
                                       @PathVariable String courseId){
        List<ChapterVo> chapterVoList = chapterService.nestedListByCourseId(courseId);
        return Result.ok().data("items", chapterVoList);
    }
}

