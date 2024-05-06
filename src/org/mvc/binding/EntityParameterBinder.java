package org.mvc.binding;

import org.mvc.MvcFile;
import org.mvc.ParameterSource;
import org.mvc.util.ConvertorUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * 实体参数绑定器 1个参数对应多个参数值<br/>
 *  每一个参数值可能有俩类 string file<br/>
 */
public class EntityParameterBinder implements ParameterBindStrategy{

    @Override
    public Object bind(Parameter parameter, ParameterSource source) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //实体绑定 通过反射获得对象的属性 and属性名 再通过属性名获得value 组成一个对象
        Class<?> type = parameter.getType();
        Object o = type.getConstructor().newInstance();
        Method[] methods = type.getMethods();
        for (Method m: methods) {
            String name = m.getName();
            if(name.startsWith("set")){
                String key = name.substring(3).toLowerCase();
                if(key.length()==1){
                    key = key.toLowerCase();
                }else{
                    key = key.substring(0, 1).toLowerCase().concat(key.substring(1));
                }
                Class<?> parameterType = m.getParameterTypes()[0];
                Object value ;
                if(parameterType == MvcFile.class || parameterType == MvcFile[].class){
                    value = source.getFileParam(key);
                }else{
                    value = source.getStringParam(key);
                }

                //到此获得的value可能是[]类型 还是需要转换类型
                try {
                    value = ConvertorUtil.Convert(value,parameterType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                m.invoke(o, value);
            }
        }
        return o;
    }

    @Override
    public boolean isSupport(Parameter parameter) {
        return true;
    }

}
