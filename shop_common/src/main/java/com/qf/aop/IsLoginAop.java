package com.qf.aop;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Aspect
@Component
public class IsLoginAop {

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("@annotation(IsLogin)")
    public Object isLogin(ProceedingJoinPoint proceedingJoinPoint){

        String loginToken = null;

        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();

        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for (Cookie cookie : cookies) {
                if("loginToken".equals(cookie)){
                    loginToken = cookie.getValue();
                    break;
                }
            }
        }

        User user=null;

        if(loginToken != null){
            user= (User) redisTemplate.opsForValue().get(loginToken);
        }

        //空，需要登录
        if(user == null){

            MethodSignature methodSignatureature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignatureature.getMethod();
            IsLogin isLogin = method.getAnnotation(IsLogin.class);
            boolean flag = isLogin.mustLogin();
            if(flag){
                String returnUrl = request.getRequestURL().toString()+"?"+request.getQueryString();

                try {

                    returnUrl = URLEncoder.encode(returnUrl,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String loginUrl = "http://localhost:8890/sso/toLogin?returnUrl=" + returnUrl;

                return "redirect:" + loginUrl;
            }

          /*  MethodSignature methodSignatureature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignatureature.getMethod();
            IsLogin isLogin = method.getAnnotation(IsLogin.class);
            boolean flag = isLogin.mustLogin();
            if(flag){
                LoginStatus.setUser(user);

                Object result = null;
                try {
                    result = proceedingJoinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                return result;
            }*/


        }


        //user非空，

        LoginStatus.setUser(user);

        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        LoginStatus.setUser(null);

        return result;
    }

}
