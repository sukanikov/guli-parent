package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.VideoVo;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public boolean removeChapterById(String id){
        //删除课时
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", id);
        videoMapper.delete(videoQueryWrapper);

        //删除章节
        return this.removeById(id);
    }

    @Override
    public List<ChapterVo> nestedListByCourseId(String courseId){

        List<ChapterVo> chapterVoArrayList = new ArrayList<>();

        //查询当前课程下所有章节信息
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterQueryWrapper.orderByAsc("sort", "id");
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);

        //查询当前课程下所有课时信息
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        videoQueryWrapper.orderByAsc("sort", "id");
        List<Video> videoList = videoMapper.selectList(videoQueryWrapper);

        //组装chapterVo，然后把chapter放进list
        for(int i = 0; i < chapterList.size(); i++){
            Chapter chapter = chapterList.get(i);

            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoArrayList.add(chapterVo);

            //根据当前chapter的id组装videoList
            List<VideoVo> videoVoArrayList = new ArrayList<>();
            for(int j = 0; j < videoList.size(); j++){
                Video video = videoList.get(j);

                if(chapter.getId().equals(video.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoArrayList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoVoArrayList);
        }

        return chapterVoArrayList;
    }

}
