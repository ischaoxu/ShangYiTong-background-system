package com.atguigu.common.service.exception;


import com.atguigu.common.util.result.Result;
import com.atguigu.common.util.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理类
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(GuiguException.class)
    public Result error(GuiguException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.fail().message(e.getMsg()).code(e.getCode());
    }

    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.build(null, ResultCodeEnum.PERMISSION);
    }

}
