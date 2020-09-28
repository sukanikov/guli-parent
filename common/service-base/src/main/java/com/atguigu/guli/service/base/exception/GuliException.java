package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-24 19:01
 */
@Data
public class GuliException extends RuntimeException {

    //状态码
    private Integer code;

    /**
     * 接收枚举类型
     * @param resultCodeEnum
     */
    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString(){
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
