package com.travel.global.exception.handler;

import com.travel.global.exception.EmailException;
import com.travel.global.exception.type.ErrorType;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("error message : {}", ex.getMessage());
        throw new EmailException(ErrorType.SERVER_ERROR);
    }
}
