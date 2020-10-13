package com.atguigu.guli.service.ucenter.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-13 20:45
 */
@Data
@ApiModel("用户登录表单对象")
public class LoginForm {
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "密码")
    private String password;
}