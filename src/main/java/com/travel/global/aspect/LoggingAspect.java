package com.travel.global.aspect;

import java.lang.reflect.Method;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Aspect
@Component
public class LoggingAspect {

    private final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    private final Logger warnLogger = LoggerFactory.getLogger("warnLogger");
    private final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    // com.travel.domain 이하 패키지의 모든 클래스 이하 모든 메서드에 적용
    @Pointcut("execution(* com.travel.domain..*.*(..))")
    private void cut() {
    }

    // Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
    // JoinPoint 객체를 통해 메서드와 파라미터에 접근
    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        infoLogger.info("======= Entering method: {} =======", method.getName());

        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            infoLogger.info("No parameters.");
        } else {
            for (Object arg : args) {
                if (arg == null) {
                    infoLogger.info("Parameter type: null");
                    infoLogger.info("Parameter value: null");
                } else {
                    infoLogger.info("Parameter type: {}", arg.getClass().getSimpleName());
                    infoLogger.info("Parameter value: {}", arg);
                }
            }
        }
    }

    // Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        infoLogger.info("======= Exiting method: {} =======", method.getName());

        if (returnObj != null) {
            if (returnObj instanceof List) {
                List<?> returnList = (List<?>) returnObj;
                infoLogger.info("Return type: List, size: {}", returnList.size());
//                for (int i = 0; i < returnList.size(); i++) {
//                    infoLogger.info("Element {}: {}", i, returnList.get(i).toString());
//                }
            } else {
                infoLogger.info("Return type: {}", returnObj.getClass().getSimpleName());
                infoLogger.info("Return value: {}", returnObj);
            }
        } else {
            warnLogger.warn("Return value is null");
        }
    }

    // 예외 발생 시 로깅
    @AfterThrowing(value = "cut()", throwing = "exception")
    public void afterThrowingLog(JoinPoint joinPoint, Throwable exception) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);

        HttpStatusCode status = getStatusCode(exception);
        if (status.is4xxClientError()) {
            warnLogger.warn("======= Exception in method: {} =======", method.getName());
            warnLogger.warn("Exception type: {}", exception.getClass().getSimpleName());
            warnLogger.warn("Exception message: {}", exception.getMessage());
        } else {
            errorLogger.error("======= Exception in method: {} =======", method.getName());
            errorLogger.error("Exception type: {}", exception.getClass().getSimpleName());
            errorLogger.error("Exception message: {}", exception.getMessage());
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
        infoLogger.info("Execution time of method {}: {} ms", getMethod(joinPoint).getName(),
            elapsedTime);
    }
}
