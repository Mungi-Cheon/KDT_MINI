package com.travel.global.exception;

import com.travel.global.exception.type.ErrorType;
import org.springframework.web.client.HttpStatusCodeException;

public class EmailException extends HttpStatusCodeException {

    public EmailException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}


