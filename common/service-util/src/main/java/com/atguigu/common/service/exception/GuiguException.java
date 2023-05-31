package com.atguigu.common.service.exception;

import com.atguigu.common.util.result.ResultCodeEnum;
import lombok.Data;

/**
 * 自定义全局异常类
 *
 */
@Data
public class GuiguException extends RuntimeException {

    //状态码
    private Integer code;
    //消息：命名为msg，不覆盖重要的系统错误message
    private String msg;

    public GuiguException(ResultCodeEnum resultCodeEnum, Throwable cause) {
        super(cause);
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    public GuiguException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    public GuiguException(Integer code, String msg, Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public GuiguException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
