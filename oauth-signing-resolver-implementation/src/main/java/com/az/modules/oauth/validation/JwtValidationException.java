package com.az.modules.oauth.validation;

public class JwtValidationException extends RuntimeException {

    public JwtValidationException(String message, Throwable ex){
        super(message, ex);
    }

	public JwtValidationException(String message){
        super(message);
    }

}
