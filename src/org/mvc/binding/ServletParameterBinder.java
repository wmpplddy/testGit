package org.mvc.binding;


import org.mvc.ParameterSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Parameter;

/**
 * Servlet��ض������ <br/>
 */
public class ServletParameterBinder  implements ParameterBindStrategy{
    @Override
    public Object bind(Parameter parameter, ParameterSource source) {
        Class<?> type = parameter.getType();
        if(type == HttpServletRequest.class){
            return source.getHttpServletRequest();
        }else if(type == HttpServletResponse.class){
            return source.getHttpServletResponse();
        }else if(type == HttpSession.class){
            return source.getHttpSession();
        }
        return null;
    }

    @Override
    public boolean isSupport(Parameter parameter) {
        // ֻ���req resp session �����ǲ���Ҫע�� ֱ��ͨ�����������ж�
        Class<?> type = parameter.getType();
        return type == HttpServletRequest.class ||
                type == HttpServletResponse.class ||
                type == HttpSession.class;

    }
}
