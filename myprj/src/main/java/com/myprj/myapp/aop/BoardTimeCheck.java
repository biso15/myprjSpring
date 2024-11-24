package com.myprj.myapp.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class BoardTimeCheck {

	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);

	@Around("execution(* com.myaws.myapp.service.BoardService*.*(..))")  // Around : ���۰� ���� ��� ����
	public Object timelog(ProceedingJoinPoint pjp) throws Throwable {
		
		Object result = null;
		
		logger.info("�����ϴ� aop");
		logger.info(Arrays.toString(pjp.getArgs()));
		long startTime = System.currentTimeMillis();  // �ý��� ��¥
		
		result = pjp.proceed();
		
		long endTime = System.currentTimeMillis();
		logger.info("������ aop");
		
		long durTime = endTime - startTime;
		logger.info(pjp.getSignature().getName() + " �ɸ��ð� : " + durTime);
		
		return result;
	}
}
