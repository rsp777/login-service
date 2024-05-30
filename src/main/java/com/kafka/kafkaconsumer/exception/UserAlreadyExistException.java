package com.kafka.kafkaconsumer.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends Exception {
		
	public UserAlreadyExistException(String message) {
        super(message);
    }
	
}
