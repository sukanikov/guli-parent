package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.CourseDescription;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.mapper.CourseDescriptionMapper;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    /**
     * 同时保存course和courseDescription
     * @param courseInfoForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)   //涉及到两张表操作，故需要事务管理
    public String saveCourseInfo(CourseInfoForm courseInfoForm){
        //保存course
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);   //比从courseInfoForm中一个个取出来然后赋值给course方便
        course.setStatus(Course.COURSE_DRAFT);

        baseMapper.insert(course);  //baseMapper是自带的，因为传入了CourseMapper（泛型）

        //保存CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());

        courseDescriptionMapper.insert(courseDescription);  //保存其他表时，就需要注入

        return course.getId();
    }

    /**
     * 需要同时查course表和courseDescription表（两表联查）
     * @param id
     * @return
     */
    @Override
    public CourseInfoForm getCourseInfoById(String id){
        //查询course
        Course course = baseMapper.selectById(id);
        if(null == course){
            return null;
        }
        //查询courseDescription，因为课程可能没有描述信息，所以结果可以为空
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        //组装成courseInfoForm对象
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        if(null != courseDescription){
            courseInfoForm.setDescription(courseDescription.getDescription());
        }

        return courseInfoForm;
    }

    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm){
        //更新Course
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);

        //更新CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        int result = courseDescriptionMapper.updateById(courseDescription);
        if(result == 0){
            courseDescriptionMapper.insert(courseDescription);
        }
    }

    @Override
    public Page<CourseVo> selectPage(Long page, Long limit, CourseQuery courseQuery){
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        String title = courseQuery.getTitle();

        QueryWrapper<CourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("c.publish_time");

        if(!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("c.teacher_id", teacherId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("c.subject_parent_id", subjectParentId);
        }
        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("c.subject_id", subjectId);
        }
        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("c.title", title);
        }

        Page<CourseVo> pageParam = new Page<>(page, limit);
        List<CourseVo> courseVoList = baseMapper.selectPageByCourseQuery(pageParam, queryWrapper);
        pageParam.setRecords(courseVoList);

        return pageParam;
    }
}
