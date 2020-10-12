package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {

    //注意别导错@Param
    List<CourseVo> selectPageByCourseQuery(Page<CourseVo> pageParam,
                                           @Param(Constants.WRAPPER) QueryWrapper<CourseVo> queryWrapper);

    CoursePublishVo selectCoursePublishVoById(String id);

    int updateViewCountById(String id);

    WebCourseVo selectWebCourseVoById(String id);
}
