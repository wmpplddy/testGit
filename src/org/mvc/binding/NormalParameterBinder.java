package org.mvc.binding;


import org.mvc.MvcFile;
import org.mvc.ParameterSource;
import org.mvc.annotations.RequestParam;
import org.mvc.exceptions.BindParameterException;
import org.mvc.util.ConvertorUtil;

import java.lang.reflect.Parameter;

/**
 * 普通参数绑定器 1:1 形式 <br/>
 * 参数值两类 ： String File
 */
public class NormalParameterBinder implements ParameterBindStrategy {

    public Object bind(Parameter parameter, ParameterSource source) {
        RequestParam annotation = parameter.getAnnotation(RequestParam.class);
        String key = annotation.value();
        Class<?> type = parameter.getType();
        Object value;
        if(type == MvcFile.class || type == MvcFile[].class){
            value = source.getFileParam(key);
        }else{
            value = source.getStringParam(key);
        }
        //到此获得了绑定的数据 但是数据还在初始状态 需要对value进行类型转换处理
        //转换是针对方法参数列表的准确类型
        //typeCast(value,parameterType)
        try {
            value = ConvertorUtil.Convert(value,type);
        } catch (Exception e) {
            throw new BindParameterException("参数获取及转换问题! 如果source为null 请检查key是否存在！");
        }finally {
            return value;
        }
    }

    @Override
    public boolean isSupport(Parameter parameter) {
        return parameter.getAnnotation(RequestParam.class) != null ;
    }
}
