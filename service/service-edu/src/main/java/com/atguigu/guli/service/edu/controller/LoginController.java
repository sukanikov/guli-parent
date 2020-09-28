package com.atguigu.guli.service.edu.controller;

import com.atguigu.guli.service.base.result.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-22 20:50
 */
@CrossOrigin //跨域
@RestController
@RequestMapping("/user")
public class LoginController {

    /**
     * 登录
     * @return
     */
    @PostMapping("login")
    public Result login() {

        return Result.ok().data("token","admin");
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info() {

        return Result.ok()
                .data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}