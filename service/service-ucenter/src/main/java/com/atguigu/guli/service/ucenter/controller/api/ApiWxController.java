package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.util.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-14 18:05
 */
@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController{
    @Autowired
    private UcenterProperties ucenterProperties;

    @Autowired
    private MemberService memberService;


    /**
     * 用户请求第三方网站（谷粒学院）进行微信登录，首先在页面会打开一个二维码，
     * 微信用户使用微信扫描二维码并且确认登录后重定向到redirectUri的网站上，并
     * 带有code和state参数（若用户拒绝授权，则只有state参数）
//     * @param session
     * @return
     */
    @GetMapping("/login")
    public String genQrConnect(HttpSession session){
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //处理回调地址
        String redirectUri = "";
        try {
            redirectUri = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //处理state
        String state = UUID.randomUUID().toString();
        session.setAttribute("wx_open_state", state);
        log.info("生成state = " + state);

        String url = String.format(baseUrl, ucenterProperties.getAppId(), redirectUri, state);

        return "redirect:" + url;
    }

    /**
     * 在回调函数中，通过code获取到access_token，再通过access_token从微信资源服务器中获取个人信息
     * @param code
     * @param state
     * @param session
     * @return
     */
    @GetMapping("/callback")
    public String callback(String code, String state, HttpSession session){
        log.info("code = " + code);
        log.info("回调state = " + state);

        //判断code是否为空（用户拒绝授权）或state是否一致（防止有人拦截并篡改请求）
        String sessionState = (String)session.getAttribute("wx_open_state");

        if(StringUtils.isEmpty(code) || !state.equals(sessionState)){
            log.error("非法的请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //使用微信定义好的远程调用接口地址（用map组装属性参数）
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> accessTokenParam = new HashMap<>();
        accessTokenParam.put("appid", ucenterProperties.getAppId());
        accessTokenParam.put("secret", ucenterProperties.getAppSecret());
        accessTokenParam.put("code", code);
        accessTokenParam.put("grant_type", "authorization_code");

        //使用地址和参数创建远程调用客户端对象
        HttpClientUtils client = new HttpClientUtils(accessTokenUrl, accessTokenParam);

        //将远程请求设置为https类型的
        client.setHttps(true);

        //获取access_token和openId
        String result = "";
        try {
            //使用客户端对象发送远程请求，并得到响应结果，存入content，通过源码，发现本质是调用了httpComponent的HttpGet（微信文档中没写，但经过测试，是使用get方式调用）
            client.get();
            //从client对象中获取远程请求调用得到的响应结果
            result = client.getContent();
            log.info("result = " + result);

        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //将json格式的result转为map
        Gson gson = new Gson();
        HashMap<String, Object> resultMap = gson.fromJson(result, HashMap.class);

        //处理错误返回
        Object errcodeObj = resultMap.get("errcode");
        if(errcodeObj != null){
            String errmsg = (String)resultMap.get("errmsg");
            Double errCode = (Double)errcodeObj;
            log.error("获取access_token失败：code = " + errCode + "，errmsg = " + errmsg);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        String accessToken = (String)resultMap.get("access_token");
        String openid = (String)resultMap.get("openid");

        //查询数据库中是否存在当前微信用户信息（若不存在，则向微信资源服务器获取用户信息，步骤和上面一样）
        Member member = memberService.getByOpenid(openid);
        if(member == null){
            //向微信服务器发起请求，携带access_token访问用户信息资源
            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=" + accessToken +
                    "&openid=" + openid;

            client = new HttpClientUtils(userInfoUrl);
            client.setHttps(true);
            String resultUserInfo = "";
            try{
                client.get();
                resultUserInfo = client.getContent();
                log.info(resultUserInfo);
            } catch(Exception e){
                log.error("获取用户信息失败");
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //解析json
            HashMap<String, Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);

            //处理错误
            Object userInfoErrcodeObj = resultUserInfoMap.get("errcode");
            if(userInfoErrcodeObj != null){
                String errmsg = (String) resultUserInfoMap.get("errmsg");
                Double errCode = (Double) userInfoErrcodeObj;
                log.error("获取用户信息失败：code = " + errCode + "，errmsg = " + errmsg);
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //获取微信个人信息
            String nickname = (String)resultUserInfoMap.get("nickname");
            String headimgurl = (String)resultUserInfoMap.get("headimgurl");
            Double sex = (Double)resultUserInfoMap.get("sex");

            //创建Member对象进行用户注册
            member = new Member();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            member.setSex(sex.intValue());
            //member.setMobile(null);//当属性为null时，sql语句自动过滤了这个字段
            memberService.save(member);
        }

        //生成JWT
        JwtInfo jwtInfo = new JwtInfo(
                member.getId(),
                member.getNickname(),
                member.getAvatar());

        String jwt = JwtHelper.createToken(jwtInfo);

        //由于此接口是被微信调用的，如果要前端服务器接收jwt，则需要redirect
        return "redirect:http://localhost:3000?token=" + jwt;
    }
}
