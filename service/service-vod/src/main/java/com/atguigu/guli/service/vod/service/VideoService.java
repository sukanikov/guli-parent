package com.atguigu.guli.service.vod.service;

import java.io.InputStream;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-01 23:48
 */
public interface VideoService{
    String uploadVideo(InputStream inputStream, String originalFilename);

    void removeVideo(String videoSourceId);
}
