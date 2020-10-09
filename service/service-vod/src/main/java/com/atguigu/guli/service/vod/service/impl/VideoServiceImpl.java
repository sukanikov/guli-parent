package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.service.VideoService;
import com.atguigu.guli.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-01 23:49
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService{
    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream inputStream, String fileName){
        //创建请求对象，并设置请求参数
        String title = fileName.substring(0, fileName.lastIndexOf("."));
        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyId(),
                vodProperties.getKeySecret(),
                title,
                fileName, inputStream);

        /* 模板组ID(可选) */
        request.setTemplateGroupId(vodProperties.getTemplateGroupId());

        //创建客户端远程连接对象
        UploadVideoImpl uploader = new UploadVideoImpl();

        //使用客户端对象想阿里云发送请求，并得到响应
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        String videoId = response.getVideoId();
        if(StringUtils.isEmpty(videoId)){
            String code = response.getCode();
            String message = response.getMessage();
            log.error("阿里云上传失败：" + code +  " - " + message);
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

        return videoId;
    }

    @Override
    public void removeVideo(String videoSourceId){
        try {
            //初始化vod客户端对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    vodProperties.getKeyId(),
                    vodProperties.getKeySecret());
            DeleteVideoRequest request = new DeleteVideoRequest();
            //id列表： "1,2,3,4,。。。。" 最多20个
            request.setVideoIds(videoSourceId);
            client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

}
