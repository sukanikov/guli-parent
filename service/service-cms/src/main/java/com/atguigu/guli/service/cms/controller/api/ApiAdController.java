package com.atguigu.guli.service.cms.controller.api;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-12 18:25
 */
@CrossOrigin //解决跨域问题
@Api(tags = "广告推荐")
@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController{
    @Autowired
    private AdService adService;

    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("list/{adTypeId}")
    public Result listByAdTypeId(
            @ApiParam(value = "推荐位id", required = true)
            @PathVariable String adTypeId) {

        List<Ad> ads = adService.selectByAdTypeId(adTypeId);
        return Result.ok().data("items", ads);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("save-value")
    public Result saveValue(){
        redisTemplate.opsForValue().set("user:13766816630", "Helen", 5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("code:13766816630", "1234");
        redisTemplate.opsForValue().set("name2", new Ad().setSort(1).setTitle("广告"));

        return Result.ok();
    }

    @GetMapping("get-value")
    public Result getValue(){
        Ad ad = (Ad)redisTemplate.opsForValue().get("name2");   //取值时，自动进行json反序列化
        return Result.ok().data("ad", ad);
    }

    @DeleteMapping("remove-value")
    public Result removeValue(){
        Boolean isDeleted = redisTemplate.delete("name2");  //是否删除成功
        Boolean hasKey = redisTemplate.hasKey("name2"); //有没有这个value
        return Result.ok().data("isDeleted", isDeleted).data("hasKey", hasKey);
    }
}
