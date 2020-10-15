package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.util.MD5;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Bailiang
 * @since 2020-10-13
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public void register(RegisterForm registerForm){
        //检查用户是否被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", registerForm.getMobile());
        Integer count = baseMapper.selectCount(queryWrapper);

        if(count > 0){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        //向数据库中添加用户
        Member member = new Member();
        member.setNickname(registerForm.getNickname());
        member.setMobile(registerForm.getMobile());
        member.setAvatar(Member.AVATAR);
        String encryptPassword = MD5.encrypt(registerForm.getPassword());
        member.setPassword(encryptPassword);
        member.setDisabled(false);

        baseMapper.insert(member);
    }

    @Override
    public String login(LoginForm loginForm){
        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(queryWrapper);

        //校验手机号
        if(member == null){
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //校验密码
        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //检查用户是否被禁用
        if(member.getDisabled()){
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //以上步骤都通过后，生成JWT
        JwtInfo jwtInfo = new JwtInfo(
                member.getId(),
                member.getNickname(),
                member.getAvatar());

        String token = JwtHelper.createToken(jwtInfo);

        return token;
    }

    @Override
    public Member getByOpenid(String openid){
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);

        return baseMapper.selectOne(queryWrapper);
    }
}
