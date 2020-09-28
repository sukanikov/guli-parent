package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.listener.ExcelSubjectDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Bailiang
 * @since 2020-09-19
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public void batchImport(InputStream inputStream){
        EasyExcel.read(inputStream,
                ExcelSubjectData.class,
                new ExcelSubjectDataListener(subjectMapper))    //注意这里要传入容器的mapper，监听器的mapper只是定义
                .excelType(ExcelTypeEnum.XLS).sheet().doRead();
    }

    @Override
    public List<SubjectVo> nestedList(){
        return baseMapper.selectNestedListByParentId("0");
    }

    @Override
    public List<SubjectVo> nestedList1(){
        ArrayList<SubjectVo> subjectVoArrayList = new ArrayList<>();

        //查询所有数据
        List<SubjectVo> subjects = baseMapper.selectSubjectVoList();

        HashMap<String, SubjectVo> map = new HashMap<>();
        for (SubjectVo cur : subjects) {
            String id = cur.getId();
            map.put(id, cur);
        }

        for (SubjectVo cur : subjects) {
            String curPid = cur.getParentId();
            if("0".equals(curPid)){
                //说明是顶级菜单节点，添加到subjectVoArrayList中
                subjectVoArrayList.add(cur);
            }else{
                //找到父亲节点，添加到他的儿子list中
                SubjectVo parentNode = map.get(curPid);
                parentNode.getChildren().add(cur);  //parentNode和subjectVoArrayList中的父节点始终都是一个，因为没有新创建对象，都是引用
            }
        }
        return subjectVoArrayList;
    }
}
