package org.mvc.exceptions;

/**
 * �����ļ�δ�ҵ�ʱ���쳣
 */
public class ConfigFileNotFoundException extends RuntimeException{
    public ConfigFileNotFoundException(){}
    public ConfigFileNotFoundException(String msg){
        super(msg);
    }
}
