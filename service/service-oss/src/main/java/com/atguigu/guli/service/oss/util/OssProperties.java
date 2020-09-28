package com.atguigu.guli.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-24 15:03
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss") //自动注入配置文件指定内容
public class OssProperties{
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
