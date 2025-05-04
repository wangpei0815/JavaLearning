package com.itheima.exception;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 所有异常
    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("程序异常:", e);
        return Result.error("出错了,请联系管理员");
    }

    // 新增信息重复异常
    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("程序异常:", e);
        String message = e.getMessage();
        String errorMessage = message.substring(message.indexOf("Duplicate entry"));
        String duplicateInfo = errorMessage.split(" ")[2];
        return Result.error(duplicateInfo + "已存在");
    }
}
