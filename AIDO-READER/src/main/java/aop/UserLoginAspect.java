package aop;

import javax.servlet.http.HttpSession;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.MBR;


@Component
@Aspect		//AOP 실행 클래스
@Order(1)	//AOP 실행 순서
public class UserLoginAspect {
	@Around("execution(* controller.Main*.*(..)) && args(..,session)")
	public Object userLoginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable {

		MBR mbr = (MBR)session.getAttribute("MBR");
		if(mbr == null) {
			throw new LoginException("로그인 해 주세요. ","LOGIN.ai");

			//throw new LoginException("LOGIN PLEASE","LOGIN.ai");
		}
		return joinPoint.proceed();
	}
}