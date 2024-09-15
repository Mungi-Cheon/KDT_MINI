package com.travel.global.exception;

import com.travel.global.exception.type.ErrorType;
import org.springframework.web.client.HttpStatusCodeException;

public class AuthException extends HttpStatusCodeException {

    public AuthException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

