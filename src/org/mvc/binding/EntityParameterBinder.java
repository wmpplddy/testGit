package org.mvc.binding;

import org.mvc.MvcFile;
import org.mvc.ParameterSource;
import org.mvc.util.ConvertorUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * ʵ��������� 1��������Ӧ�������ֵ<br/>
 *  ÿһ������ֵ���������� string file<br/>
 */
public class EntityParameterBinder implements ParameterBindStrategy{

    @Override
    public Object bind(Parameter parameter, ParameterSource source) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //ʵ��� ͨ�������ö�������� and������ ��ͨ�����������value ���һ������
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

                //���˻�õ�value������[]���� ������Ҫת������
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
