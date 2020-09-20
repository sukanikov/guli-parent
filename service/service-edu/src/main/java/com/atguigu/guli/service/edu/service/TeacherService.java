package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.query.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
public interface TeacherService extends IService<Teacher> {
    Page<Teacher> selectPage(Page<Teacher> pageParam, TeacherQuery teacherQuery);
}
