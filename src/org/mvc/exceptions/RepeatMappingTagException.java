package org.mvc.exceptions;

/**
 * 一个重复mapping配置异常
 */
public class RepeatMappingTagException extends RuntimeException{
    public RepeatMappingTagException(){}
    public RepeatMappingTagException(String msg){super(msg);}
}
