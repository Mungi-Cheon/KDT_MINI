package com.travel.global.exception;

import com.travel.global.exception.type.ErrorType;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class AccommodationException extends HttpStatusCodeException {

    public AccommodationException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

