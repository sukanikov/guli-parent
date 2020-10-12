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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

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
}
