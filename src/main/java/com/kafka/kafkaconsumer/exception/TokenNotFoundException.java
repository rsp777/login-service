package com.kafka.kafkaconsumer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenNotFoundException extends Exception {

	public TokenNotFoundException(String message, String token) {
        super(message);
    }
	
}
