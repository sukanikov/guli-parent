package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Repository
public interface SubjectMapper extends BaseMapper<Subject> {
    List<SubjectVo> selectNestedListByParentId(String parentId);

    List<SubjectVo> selectSubjectVoList();
}
