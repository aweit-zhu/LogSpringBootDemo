package com.example.demo.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.annotation.LogAction;
import com.example.demo.dto.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogActionAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogActionAspect.class);

	@Autowired
	private HttpServletRequest request;

	@Around("@annotation(com.example.demo.annotation.LogAction)")
	public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {

		// 取得URI
		String url = request.getRequestURI();
		
		// 取得方法
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

		// 取得註解
		LogAction logAction = method.getAnnotation(LogAction.class);
		
		// 用戶名稱
		String username = "";

		if (logAction.name().equals("登入")) {
			Object[] args = joinPoint.getArgs();
			LoginRequest loginRequest = (LoginRequest) args[0];
			username = loginRequest.getUsername();
		} else {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			username = authentication != null ? authentication.getName() : "Anonymous";
		}
		
		// 執行方法，與取得結果
		Object result = joinPoint.proceed();

		if (result instanceof ResponseEntity<?>) {
			ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
			logger.info("Username: {}, LogAction: {}, URL: {}, Result: {}, Status: {}", username, logAction.name(), url,
					responseEntity.getBody(), responseEntity.getStatusCode());
		} else {
			logger.info("Username: {}, LogAction: {}, URL: {}, Result: {}", username, logAction.name(), url, result);
		}
		return result;
	}
}
