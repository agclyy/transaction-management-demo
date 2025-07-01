package com.demo.lyy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Aspect
@Component
public class RequestLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingAspect.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RequestLoggingAspect() {
        // Register Java 8 date and time type support module
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Pointcut("within(com.demo.lyy.controller..*)")
    public void requestEndpoint() {
    }

    @Around("requestEndpoint()")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        try {
            Object result = joinPoint.proceed();
            log.info("\n" +
                            "HTTP Method: {} \n" +
                            "URL: {} \n" +
                            "IP: {} \n" +
                            "Class Method: {}.{} \n" +
                            "request: {} \n" +
                            "Response: {} \n" +
                            "Time Taken: {} ms",
                    request.getMethod(),
                    request.getRequestURL().toString(),
                    request.getRemoteAddr(),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    maskSensitiveData(objectMapper.writeValueAsString(joinPoint.getArgs())),
                    maskSensitiveData(objectMapper.writeValueAsString(result)),
                    System.currentTimeMillis() - startTime);
            return result;
        } catch (Exception e) {
            log.error("Exception in {}.{}: {}", className, methodName, e.getMessage());
            throw e;
        }
    }

    private String maskSensitiveData(String json) {
        // todo desensitizing sensitive data
        return json;
    }
}
