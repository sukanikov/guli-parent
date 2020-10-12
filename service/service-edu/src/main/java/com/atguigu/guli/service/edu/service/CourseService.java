package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoById(String id);

    void updateCourseInfo(CourseInfoForm courseInfoForm);

    Page<CourseVo> selectPage(Long page, Long limit, CourseQuery courseQuery);

    boolean removeCoverById(String id);

    boolean removeCourseById(String id);

    CoursePublishVo getCoursePublishByVoId(String id);

    boolean publishCourseById(String id);

    List<Course> webSelectList(WebCourseQuery webCourseQuery);

    WebCourseVo selectWebCourseVoById(String id);
}
