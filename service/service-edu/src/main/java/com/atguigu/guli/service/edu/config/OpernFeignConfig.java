package com.atguigu.guli.service.edu.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-26 22:22
 */
@Configuration
public class OpernFeignConfig{
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
