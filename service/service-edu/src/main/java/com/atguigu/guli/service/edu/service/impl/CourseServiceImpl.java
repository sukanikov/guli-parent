package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
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
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseCollectMapper courseCollectMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Autowired
    private OssFileService ossFileService;

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

    @Override
    public boolean removeCoverById(String id){
        Course course = baseMapper.selectById(id);
        String cover = course.getCover();
        if(!org.apache.commons.lang3.StringUtils.isEmpty(cover)){
            Result result = ossFileService.removeFile(cover);
            return result.getSuccess();
        }
        return false;
    }

    /**
     * 删除课程时顺带还要将章节，课时，课程详情，视频等一并删除（与courseId有关的都删），涉及到多张表
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id){
        //删除评论信息 Comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //删除收藏信息 CourseCollect（根据业务需求，不删的话给用户显示空数据，让用户自己删）
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //删除课时信息 Video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //删除章节信息 Chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //删除课程详情 CourseDescription
        courseDescriptionMapper.deleteById(id);

        //最后删除课程
        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishByVoId(String id){
        return baseMapper.selectCoursePublishVoById(id);
    }

    //本质就是把后台管理系统第二步已保存的课程信息改成已发布的要求
    @Override
    public boolean publishCourseById(String id){
        Course course = new Course();
        course.setId(id);
        course.setPublishTime(new Date());
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQuery webCourseQuery){
        //只有课程“已发布”，才能显示
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", Course.COURSE_NORMAL);

        String subjectParentId = webCourseQuery.getSubjectParentId();
        String subjectId = webCourseQuery.getSubjectId();
        String buyCountSort = webCourseQuery.getBuyCountSort();
        String publishTimeSort = webCourseQuery.getPublishTimeSort();
        String priceSort = webCourseQuery.getPriceSort();

        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }

        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id", subjectId);
        }

        if(!StringUtils.isEmpty(buyCountSort)){
            queryWrapper.orderByDesc("buy_count");
        }

        if(!StringUtils.isEmpty(publishTimeSort)){
            queryWrapper.orderByDesc("publish_time");
        }

        if(!StringUtils.isEmpty(priceSort)){
            if("1".equals(priceSort)){
                queryWrapper.orderByAsc("price");
            }else{
                queryWrapper.orderByDesc("price");
            }
        }

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public WebCourseVo selectWebCourseVoById(String id){
        baseMapper.updateViewCountById(id);

        return baseMapper.selectWebCourseVoById(id);
    }
}
