package com.travel.global.exception;

import com.travel.global.exception.type.ErrorType;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductException extends HttpStatusCodeException {

    public ProductException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

