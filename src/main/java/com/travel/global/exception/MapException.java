package com.travel.global.exception;

import com.travel.global.exception.type.ErrorType;
import org.springframework.web.client.HttpStatusCodeException;

public class MapException extends HttpStatusCodeException {

    public MapException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
