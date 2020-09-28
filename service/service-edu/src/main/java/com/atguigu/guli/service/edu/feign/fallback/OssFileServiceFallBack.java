package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-26 22:38
 */
@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService{

    @Override
    public Result removeFile(String url){
        log.warn("远程调用失败，服务熔断");
        return Result.error();
    }
}
