package com.travel.global.aspect;

import com.travel.global.annotation.DistributedLock;
import com.travel.global.parser.CustomSpringELParser;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

//실질적인 락 동작
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.travel.global.annotation.DistributedLock)")
    public Object lock ( final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DistributedLock distributedLock  = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX +
                CustomSpringELParser.getDynamicValue(
                        methodSignature.getParameterNames(), joinPoint.getArgs(), // getParameterNames = reqlist, memberId 의 변수명, args = reqlist, memberId 의 값
                        distributedLock.key()); //키 설정 #request.requestList.![accommodationId + '-' + productId + '-' +"+ " checkInDate +'-'+ checkOutDate]
        RLock rLock = redissonClient.getLock(key); //1

        try {
            boolean available = rLock.tryLock(distributedLock.waitTime(),
                    distributedLock.leaseTime(), distributedLock.timeUnit());  // (2)
            if (!available) {
                log.info("Lock 획득 실패={}", key);
                return false;
            }

            return aopForTransaction.proceed(joinPoint);  // (3)
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();   // (4) 획득 유무와 상관 없이 unlock
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {} {}", method.getName(), key);
            }
        }
    }
}
