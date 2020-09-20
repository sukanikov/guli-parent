package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.query.TeacherQuery;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public Page<Teacher> selectPage(Page<Teacher> pageParam, TeacherQuery teacherQuery){
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        if(teacherQuery == null){
            return baseMapper.selectPage(pageParam, queryWrapper);
        }

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String joinDateBegin = teacherQuery.getJoinDateBegin();
        String joinDateEnd = teacherQuery.getJoinDateEnd();


        if(!StringUtils.isEmpty(name)){
            queryWrapper.likeRight("name", name);
        }

        if(level != null){
            queryWrapper.eq("level", level);
        }

        if(!StringUtils.isEmpty(joinDateBegin)) {
            queryWrapper.ge("join_date", joinDateBegin);
        }

        if(!StringUtils.isEmpty(joinDateEnd)) {
            queryWrapper.le("join_date", joinDateEnd);
        }

        return baseMapper.selectPage(pageParam, queryWrapper);
    }
}
