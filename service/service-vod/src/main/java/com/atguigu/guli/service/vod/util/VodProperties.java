package com.atguigu.guli.service.vod.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-10-02 00:05
 */
@Data
@Component
@ConfigurationProperties(prefix="aliyun.vod")
public class VodProperties{
    private String keyId;
    private String keySecret;
    private String templateGroupId;
    private String workflowId;
}
