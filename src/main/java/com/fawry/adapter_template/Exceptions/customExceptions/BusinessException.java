package com.fawry.adapter_template.Exceptions.customExceptions;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
