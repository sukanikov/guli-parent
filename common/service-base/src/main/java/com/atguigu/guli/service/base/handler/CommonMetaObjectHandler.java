package com.atguigu.guli.service.base.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-20 17:35
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler{

    @Override
    public void insertFill(MetaObject metaObject){
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject){
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }
}
