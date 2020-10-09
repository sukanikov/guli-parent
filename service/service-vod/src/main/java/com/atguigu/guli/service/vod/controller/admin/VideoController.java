package com.atguigu.guli.service.vod.controller.admin;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-01 23:46
 */

@Api(tags = "阿里云视频点播")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/vod/video")
@Slf4j
public class VideoController{

    @Autowired
    private VideoService videoService;

    @ApiOperation("阿里云视频上传")
    @PostMapping("/upload")
    public Result uploadVideo(
            @ApiParam(name = "file", value = "视频文件", required = true, type = "multipart/form-data")
            @RequestParam("file") MultipartFile file){

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId = videoService.uploadVideo(inputStream, originalFilename);
            return Result.ok().message("文件上传成功").data("videoId", videoId);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @ApiOperation("根据视频id执行阿里云视频删除")
    @DeleteMapping("remove/{videoSourceId}")
    public Result removeVideo(
            @ApiParam(value = "阿里云视频id", required = true)
            @PathVariable String videoSourceId){
        videoService.removeVideo(videoSourceId);
        return Result.ok().message("视频删除成功");
    }
}
