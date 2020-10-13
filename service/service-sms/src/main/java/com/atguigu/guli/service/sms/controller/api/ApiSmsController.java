package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.common.util.RandomUtils;
import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-13 18:32
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@CrossOrigin //跨域
@Slf4j
public class ApiSmsController{
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("获取验证码")
    @GetMapping("send/{mobile}")
    public Result getCode(@PathVariable String mobile){
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)){
            return Result.setResult(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        String checkCode = RandomUtils.getFourBitRandom();
        redisTemplate.opsForValue().set("checkCode:" + mobile, checkCode, 5, TimeUnit.MINUTES);

        return Result.ok().message("短信发送成功");
    }
}
