package com.coursefeedback.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionAspect {

    // Target all service layer methods
    @AfterThrowing(pointcut = "execution(* com.coursefeedback.service..*(..))", throwing = "ex")
    public void logServiceExceptions(JoinPoint joinPoint, Throwable ex) {
        String method = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        log.error(" Exception in {}.{}(): {}", className, method, ex.getMessage());
    }
}
