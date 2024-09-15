package com.travel.global.exception;

import com.travel.global.exception.type.ErrorType;
import org.springframework.web.client.HttpStatusCodeException;

public class ReservationsException extends HttpStatusCodeException {

    public ReservationsException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

