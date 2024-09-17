package com.travel.global.aspect;

import java.lang.reflect.Method;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.travel.domain..*.*(..))")
    private void cut() {
    }

    // Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
    // JoinPoint 객체를 통해 메서드와 파라미터에 접근
    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("======= Entering method: {} =======", method.getName());

        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            log.info("No parameters.");
        } else {
            for (Object arg : args) {
                if (arg == null) {
                    log.info("Parameter type: null");
                    log.info("Parameter value: null");
                } else {
                    log.info("Parameter type: {}", arg.getClass().getSimpleName());
                    log.info("Parameter value: {}", arg);
                }
            }
        }
    }

    // Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("======= Exiting method: {} =======", method.getName());

        if (returnObj != null) {
            if (returnObj instanceof List<?> returnList) {
                log.info("Return type: List, size: {}", returnList.size());
            } else {
                log.info("Return type: {}", returnObj.getClass().getSimpleName());
                log.info("Return value: {}", returnObj);
            }
        } else {
            log.warn("Return value is null");
        }
    }

    // 예외 발생 시 로깅
    @AfterThrowing(value = "cut()", throwing = "exception")
    public void afterThrowingLog(JoinPoint joinPoint, Throwable exception) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);

        HttpStatusCode status = getStatusCode(exception);
        if (status.is4xxClientError()) {
            log.warn("======= Exception in method: {} =======", method.getName());
            log.warn("Exception type: {}", exception.getClass().getSimpleName());
            log.warn("Exception message: {}", exception.getMessage());
        } else {
            log.error("======= Exception in method: {} =======", method.getName());
            log.error("Exception type: {}", exception.getClass().getSimpleName());
            log.error("Exception message: {}", exception.getMessage());
        }
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    // 예외로부터 HttpStatus를 가져오는 메서드
    private HttpStatusCode getStatusCode(Throwable exception) {
        if (exception instanceof HttpStatusCodeException) {
            return ((HttpStatusCodeException) exception).getStatusCode();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    //실행시간
    @Around("cut()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();  // 메서드 실행
        } catch (Throwable throwable) {
            logExecutionTime(joinPoint, start);
            throw throwable;
        }
        logExecutionTime(joinPoint, start);
        return result;
    }

    private void logExecutionTime(ProceedingJoinPoint joinPoint, long start) {
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Execution time of method {}: {} ms", getMethod(joinPoint).getName(),
            elapsedTime);
    }
}
