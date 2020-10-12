package com.atguigu.guli.service.vod.controller.api;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-11 16:14
 */
@Api(tags = "阿里云视频点播")
@CrossOrigin //跨域
@RestController
@RequestMapping("/api/vod/video")
@Slf4j
public class ApiVideoController{
    @Autowired
    private VideoService videoService;

    @ApiOperation("获取视频播放凭证")
    @GetMapping("get-play-auth/{videoSourceId}")
    public Result getPlayAuth(
            @ApiParam(value = "阿里云平台视频id", required = true)
            @PathVariable String videoSourceId){
        String playAuth = videoService.getPlayAuthById(videoSourceId);
        return Result.ok().data("playAuth", playAuth);
    }
}
