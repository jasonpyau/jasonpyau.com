package com.jasonpyau.annotation;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.jasonpyau.exception.RateLimitException;
import com.jasonpyau.exception.UnauthorizedException;
import com.jasonpyau.service.AuthorizationService;
import com.jasonpyau.service.RateLimitService;

import io.github.bucket4j.EstimationProbe;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizeAdminAspect {

    private final RateLimitService rateLimitService;
    
    @Around("@annotation(AuthorizeAdmin)")
    public Object authorizeAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        RateLimit rateLimitAnnotation = methodSignature.getMethod().getAnnotation(RateLimit.class);
        long token = RateLimit.ADMIN_TOKEN;
        if (rateLimitAnnotation != null) {
            token = rateLimitAnnotation.value();
        }
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest)arg;
                EstimationProbe estimationProbe = rateLimitService.estimateRateLimit(request, token);
                if (!estimationProbe.canBeConsumed()) {
                    throw new RateLimitException(TimeUnit.NANOSECONDS.toMillis(estimationProbe.getNanosToWaitForRefill()));
                } else if (!AuthorizationService.authorize(request)) {
                    rateLimitService.rateLimit(request, RateLimit.EXPENSIVE_TOKEN);
                    throw new UnauthorizedException();
                }
                return joinPoint.proceed();
            }
        }
        throw new UnauthorizedException();
    }
}
