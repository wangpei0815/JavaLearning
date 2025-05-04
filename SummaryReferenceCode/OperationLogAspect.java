package com.itheima.aop;

import com.itheima.mapper.OperatorLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
@Slf4j
@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    private OperatorLogMapper operatorLogMapper;

    @Around("@annotation(com.itheima.anno.Log)")
    public Object logOperation(ProceedingJoinPoint proceedingJoinPoint)  throws Throwable{
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        // 执行目标方法
        Object result = proceedingJoinPoint.proceed();
        // 记录结束时间,计算耗时
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        // 构建日志实体类
        OperateLog operateLog = new OperateLog(
                null,
                getCurrentEmpId(),
                LocalDateTime.now(),
                proceedingJoinPoint.getTarget().getClass().getName(),
                proceedingJoinPoint.getSignature().getName(),
                Arrays.toString(proceedingJoinPoint.getArgs()),
                result != null ? result.toString() : "void",
                costTime,
                null
        );

        log.info("记录操作日志:{}", operateLog);
        // 调用Mapper层方法,记录日志保存到数据库
        operatorLogMapper.insert(operateLog);
        // 代理结束,返回请求的返回资源
        return result;
    }

    // 从当前线程局部变量中获得当前登录的员工id
    private Integer getCurrentEmpId() {
        return CurrentHolder.getCurrentId();
    }
}
