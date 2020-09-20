package com.atguigu.guli.service.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-20 14:59
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})    //不设置的话，只扫描该微服务edu下的包，不会扫描到其他包的MybatisPlusConfig
public class ServiceEduApplication{
    public static void main(String[] args){
        SpringApplication.run(ServiceEduApplication.class, args);
    }
}
