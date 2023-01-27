package com.bobocode.vitalish.context.exception;

public class BeanPostProcessorException extends RuntimeException {

    public BeanPostProcessorException(String message, Throwable exception) {
        super(message, exception);
    }
}
