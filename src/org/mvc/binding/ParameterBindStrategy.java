package org.mvc.binding;

import org.mvc.ParameterSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public interface ParameterBindStrategy {

    /**
     * 从paramSource中获得parameter这个参数所需要的参数值 ，目前还有3种<br/>
     * 1.  1:1的键值对<br/>
     * 2.  1:n的请求参数<br/>
     * 3.  servlet相关对象的请求参数<br/>
     * @param parameter
     * @param source
     * @return
     */
    Object bind(Parameter parameter, ParameterSource source) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    /**
     * 判断当前这个绑定器 是否支持当前这个参数<br/>
     * 使用这个绑定策略能否为当前这个参数找到对应的参数值<br/>
     *
     * @return
     */
    boolean isSupport(Parameter parameter);
}
