package com.atguigu.guli.service.base.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-20 14:38
 */
@EnableTransactionManagement    //给mybatis操作添加事务处理
@Configuration
@MapperScan("com.atguigu.guli.service.*.mapper")    //也可以配在启动类
public class MybatisPlusConfig{
    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
