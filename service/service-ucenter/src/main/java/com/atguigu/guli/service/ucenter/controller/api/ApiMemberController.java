package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Bailiang
 * @since 2020-10-13
 */
@Api(tags = "会员管理")
@CrossOrigin
@RestController
@RequestMapping("api/ucenter/member")
@Slf4j
public class ApiMemberController{
    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("用户注册")
    @PostMapping("register")
    public Result register(
            @ApiParam(value = "注册表单", required = true)
            @RequestBody RegisterForm registerForm){
        //获取表单参数
        String mobile = registerForm.getMobile();
        String code = registerForm.getCode();
        String nickname = registerForm.getNickname();
        String password = registerForm.getPassword();

        //校验表单参数合法性
        if(StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickname)
                || !FormUtils.isMobile(mobile)){
            return Result.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        //从redis中取验证码，对比和用户输入是否一致
        String checkcode = (String) redisTemplate.opsForValue().get("checkCode:" + mobile);
        if(!checkcode.equals(code)){
            return Result.setResult(ResultCodeEnum.CODE_ERROR);
        }
        //若一致，就进行用户注册，向member表中添加一条记录
        memberService.register(registerForm);

        return Result.ok().message("注册成功");
    }

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public Result login(
            @ApiParam(value = "登录表单", required = true)
            @RequestBody LoginForm loginForm){
        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();

        //校验参数
        if(StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)){

            return Result.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        String token = memberService.login(loginForm);

        return Result.ok().data("token", token).message("登录成功");
    }

    /**
     *
     * @param request   前台将JWT通过请求头传到后台，后台解析JWT并返回给前台解析信息
     * @return
     */
    @ApiOperation(value = "根据token获取登录用户信息")
    @GetMapping("auth/get-login-info")
    public Result getLoginInfo(HttpServletRequest request){
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return Result.ok().data("userInfo", jwtInfo);
    }
}

