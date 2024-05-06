package org.mvc.binding;


import org.mvc.ParameterSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Parameter;

/**
 * Servlet相关对象绑定器 <br/>
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
        // 只针对req resp session 理论是不需要注解 直接通过参数类型判断
        Class<?> type = parameter.getType();
        return type == HttpServletRequest.class ||
                type == HttpServletResponse.class ||
                type == HttpSession.class;

    }
}
