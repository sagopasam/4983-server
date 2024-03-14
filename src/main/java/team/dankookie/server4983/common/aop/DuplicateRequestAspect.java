package team.dankookie.server4983.common.aop;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class DuplicateRequestAspect
{
	private Set<String> requestSet = Collections.synchronizedSet(new HashSet<>());

	@Pointcut("execution(* team.dankookie.server4983.book.controller.UsedBookController.saveSaveUsedBook(..))")
	public void onRequest() {}

	@Around("onRequest()")
	public Object duplicateRequestCheck(ProceedingJoinPoint joinPoint) throws Throwable {
		String requestId = joinPoint.getSignature().toLongString();

		log.info("requestId : " + requestId);

		if (requestSet.contains(requestId)) {
			return handleDuplicateRequest();
		}

		requestSet.add(requestId);

		try {
			return joinPoint.proceed();
		} finally {
			requestSet.remove(requestId);
		}
	}

	private ResponseEntity<Object> handleDuplicateRequest() {
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("중복된 요청 입니다");
	}
}
