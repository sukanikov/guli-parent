package com.atguigu.guli.service.oss.controller.admin;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.service.impl.FileServiceImpl;
import com.atguigu.guli.service.oss.util.OssProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-24 15:26
 */
@Slf4j
@Api(tags = "阿里云文件管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/oss/file")
public class FileController{
    @Autowired
    private OssProperties ossProperties;

    @Value("${server.port}")
    Integer port;

    @Autowired
    private FileServiceImpl fileService;

//    @GetMapping("/test")
//    public Result test(){
//        return Result.ok().data("ossProperties", ossProperties);
//    }
//
//
//    @GetMapping("/test2")
//    public Result test2(){
//        System.err.println("oss test被调用" + port);
//        return Result.ok();
//    }


    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("module") String module){
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String url = fileService.upload(inputStream, originalFilename, module);
            return Result.ok().message("文件上传成功").data("url", url);
        } catch (Exception e) {
            //打印原始的异常错误跟踪栈
            log.error(ExceptionUtils.getStackTrace(e));
            //抛出自定义异常对象：对象中包含动态设置的message和code值
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);

        }
    }

    @ApiOperation("文件删除")
    @DeleteMapping("remove")
    public Result removeFile(@RequestBody String url){
        fileService.removeFile(url);
        return Result.ok().message("文件删除成功");
    }
}
