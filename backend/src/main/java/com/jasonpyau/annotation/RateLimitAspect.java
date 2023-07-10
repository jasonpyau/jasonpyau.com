package com.jasonpyau.annotation;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.jasonpyau.exception.RateLimitException;
import com.jasonpyau.service.RateLimitService;

import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RateLimitAspect {
    
    @Around("@annotation(RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        RateLimit rateLimitAnnotation = methodSignature.getMethod().getAnnotation(RateLimit.class);
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest)arg;
                ConsumptionProbe consumptionProbe = RateLimitService.RateLimiter.rateLimit(request, rateLimitAnnotation.value());
                if (!consumptionProbe.isConsumed()) {
                    throw new RateLimitException(TimeUnit.NANOSECONDS.toMillis(consumptionProbe.getNanosToWaitForRefill()));
                }
                return joinPoint.proceed();
            }
        }
        throw new RateLimitException(Long.MAX_VALUE);
    }
}
