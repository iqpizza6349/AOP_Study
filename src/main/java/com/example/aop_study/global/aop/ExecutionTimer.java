package com.example.aop_study.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class ExecutionTimer {

    // joinPoint 를 Annotation 으로 설정
    @Pointcut("@annotation(com.example.aop_study.global.aop.annotation.ExecTimer)")
    private void timer() {}

    // method 실행 전, 후로 시간을 공유해야하기 때문
    @Around("timer()")
    public Object AssumeExecutionTimer(ProceedingJoinPoint joinPoint)
            throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();

        log.info("실행 메소드: {}, 실행 시간: {}ms", methodName, totalTimeMillis);
        return result;
    }

}
