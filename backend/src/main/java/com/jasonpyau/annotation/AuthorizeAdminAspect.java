package com.jasonpyau.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.jasonpyau.exception.UnauthorizedException;
import com.jasonpyau.service.AuthorizationService;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthorizeAdminAspect {
    
    @Around("@annotation(AuthorizeAdmin)")
    public Object AuthorizeAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest)arg;
                if (!AuthorizationService.authorize(request)) {
                    throw new UnauthorizedException();
                }
                return joinPoint.proceed();
            }
        }
        throw new UnauthorizedException();
    }
}
