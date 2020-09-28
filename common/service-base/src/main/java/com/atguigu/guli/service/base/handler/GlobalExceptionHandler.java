package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.Result;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-21 20:14
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler{
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.error();
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public Result error(BadSqlGrammarException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Result error(HttpMessageNotReadableException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    //处理自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public Result error(GuliException e){
        log.error(ExceptionUtils.getStackTrace(e));
        //从异常对象中取出动态设置的message和code值，返回给前端
        return Result.error().message(e.getMessage()).code(e.getCode());
    }
}
