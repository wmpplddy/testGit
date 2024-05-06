package org.mvc.exceptions;

public class BindParameterException extends RuntimeException{
    public BindParameterException(){}
    public BindParameterException(String msg){
        super(msg);
    }
}
