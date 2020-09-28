package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService{

    @DeleteMapping("/admin/oss/file/remove")
    Result removeFile(@RequestBody String url);
}
