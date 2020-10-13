package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.feign.OssFileService;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-09-08
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private OssFileService ossFileService;

    /**
     * 根据id删除幻灯片图片
     * @param id
     */
    @Override
    public boolean removeAdImageById(String id) {

        Ad ad = baseMapper.selectById(id);
        if(ad != null){
            String imageUrl = ad.getImageUrl();
            if(!StringUtils.isEmpty(imageUrl)){
                //删除图片：调用client方法
                Result Result = ossFileService.removeFile(imageUrl);
                return Result.getSuccess();
            }
        }
        return false;
    }

    @Override
    public void selectPage(Page<AdVo> pageParam) {
        QueryWrapper<AdVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("a.type_id", "a.sort");
        List<AdVo> records = baseMapper.selectPageByQueryWrapper(pageParam, queryWrapper);
        pageParam.setRecords(records);
    }

    //使用此注解后，首次从数据库中查询到数据，并存放在缓存一份，下次再查询该数据时，就先从缓存中查询，不经过数据库了（如果缓存中有），功能和selectByAdTypeId1一样，但不稳定，项目中也很少用
    @Cacheable(value = "index", key = "'selectByAdTypeId'")
    @Override
    public List<Ad> selectByAdTypeId(String adTypeId){
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort", "id");
        queryWrapper.eq("type_id", adTypeId);

        return baseMapper.selectList(queryWrapper);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    public List<Ad> selectByAdTypeId1(String adTypeId) {
        List<Ad> adList = null;

        try {
            //首先：从redis中查询数据是否存在
            adList = (List<Ad>)redisTemplate.opsForValue().get("index:adList");
            //如果数据存在则返回redis中的数据
            if(adList != null){
                return adList;
            }
        } catch (Exception e) {
            log.warn("redis服务异常：存值 selectByAdTypeId1");
        }
        //如果数据不存在则从数据库中获取数据
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort", "id");
        queryWrapper.eq("type_id", adTypeId);
        adList = baseMapper.selectList(queryWrapper);

        try {
            //最后：将数据存入redis
            redisTemplate.opsForValue().set("index:adList", adList, 15, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("redis服务异常：取值 selectByAdTypeId1");
        }

        return adList;
    }
}
