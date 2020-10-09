package com.atguigu.guli.service.vod;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-01 21:11
 */

@SpringBootConfiguration
@SpringBootTest
public class VodSdkTest{

    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException{
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    @Test
    void testGetPlayAuth() throws ClientException{
        //初始化vod对象
        DefaultAcsClient client = initVodClient(
                "LTAI4G3xpa14HGX5jS3dy5QD",
                "0Md4fFv5MY0UPgbyKAwYTa1v5xMuPV");

        //创建获取凭证的request
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

        //给request设置请求参数（视频id）
        request.setVideoId("968cdc0833cb4f83903eeef5020abba7");

        //获取response，并通过response获取凭证
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        //Base信息
        System.out.println("PlayAuth = " + response.getPlayAuth());
        System.out.println("VideoMeta.Title = " + response.getVideoMeta().getTitle());
        System.out.println("RequestId = " + response.getRequestId());
    }

    /**
     * 获取视频的url
     * @throws ClientException
     */
    @Test
    void testGetPlayInfo() throws ClientException{
        //初始化vod客户端对象
        DefaultAcsClient client = initVodClient(
                "LTAI4G3xpa14HGX5jS3dy5QD",
                "0Md4fFv5MY0UPgbyKAwYTa1v5xMuPV");

        //初始化请求对象，并设置请求参数（视频id）
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("e42b9b57818f47e384567cf70f8918da");

        //获取响应信息
        GetPlayInfoResponse response = client.getAcsResponse(request);

        //根据响应信息得到视频播放列表
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();

        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
