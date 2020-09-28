package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-27 19:13
 */
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData>{

    //    @Autowired  //不能注入，因为该listener不在spring容器中（文档中可查）
    private SubjectMapper subjectMapper;

    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext context){
        //一级类别
        String levelOneTitle = data.getLevelOneTitle();

        //二级类别
        String levelTwoTitle = data.getLevelTwoTitle();

        /**
         * 以下两步是防止重复向数据库表中注入
         */

        //查询一级类别是否存在
        Subject levelOneFromDB = this.getByTitle(levelOneTitle);
        String parentId = "";
        if(levelOneFromDB == null){
            //构建并存储一级类别
            Subject subjectLevelOne = new Subject();
            subjectLevelOne.setTitle(levelOneTitle);
            subjectLevelOne.setParentId("0");
            subjectMapper.insert(subjectLevelOne);
            parentId = subjectLevelOne.getId();
        } else{
            parentId = levelOneFromDB.getId();
        }


        //查询二级类别是否存在
        Subject levelTwoFromDB = this.getSubSubjectByTitle(parentId, levelTwoTitle);
        if(levelTwoFromDB == null){
            //构建并存储二级类别
            Subject subjectLevelTwo = new Subject();
            subjectLevelTwo.setTitle(levelTwoTitle);
            subjectLevelTwo.setParentId(parentId);
            subjectMapper.insert(subjectLevelTwo);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context){

    }

    private Subject getByTitle(String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");
        Subject subject = subjectMapper.selectOne(queryWrapper);
        return subject;
    }

    /**
     * 在相同的一级类别下判断是否有相同的二级类别
     *
     * @param parentId
     * @param title
     * @return
     */
    private Subject getSubSubjectByTitle(String parentId, String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);
        Subject subject = subjectMapper.selectOne(queryWrapper);
        return subject;
    }
}
