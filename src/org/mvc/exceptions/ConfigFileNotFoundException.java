package org.mvc.exceptions;

/**
 * 配置文件未找到时的异常
 */
public class ConfigFileNotFoundException extends RuntimeException{
    public ConfigFileNotFoundException(){}
    public ConfigFileNotFoundException(String msg){
        super(msg);
    }
}
