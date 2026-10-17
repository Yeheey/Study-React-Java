package com.student.manager.common;

import com.student.manager.entity.OperationLog;
import com.student.manager.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final LogService logService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.student.manager.common.Log)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        OperationLog operationLog = new OperationLog();

        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);

            operationLog.setModule(logAnnotation.module());
            operationLog.setType(logAnnotation.type());
            operationLog.setMethod(point.getTarget().getClass().getName() + "." + method.getName());

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                operationLog.setUrl(request.getRequestURI());
                operationLog.setIp(getClientIp(request));
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                operationLog.setOperator(auth.getName());
            }

            Object result = point.proceed();
            operationLog.setStatus(1);
            operationLog.setCostTime(System.currentTimeMillis() - start);
            operationLog.setCreateTime(LocalDateTime.now());
            logService.save(operationLog);
            return result;
        } catch (Throwable e) {
            operationLog.setStatus(0);
            operationLog.setCostTime(System.currentTimeMillis() - start);
            operationLog.setCreateTime(LocalDateTime.now());
            operationLog.setErrorMsg(e.getMessage());
            logService.save(operationLog);
            throw e;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
