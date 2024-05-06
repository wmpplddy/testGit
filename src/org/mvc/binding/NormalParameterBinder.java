package org.mvc.binding;


import org.mvc.MvcFile;
import org.mvc.ParameterSource;
import org.mvc.annotations.RequestParam;
import org.mvc.exceptions.BindParameterException;
import org.mvc.util.ConvertorUtil;

import java.lang.reflect.Parameter;

/**
 * ��ͨ�������� 1:1 ��ʽ <br/>
 * ����ֵ���� �� String File
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
        //���˻���˰󶨵����� �������ݻ��ڳ�ʼ״̬ ��Ҫ��value��������ת������
        //ת������Է��������б��׼ȷ����
        //typeCast(value,parameterType)
        try {
            value = ConvertorUtil.Convert(value,type);
        } catch (Exception e) {
            throw new BindParameterException("������ȡ��ת������! ���sourceΪnull ����key�Ƿ���ڣ�");
        }finally {
            return value;
        }
    }

    @Override
    public boolean isSupport(Parameter parameter) {
        return parameter.getAnnotation(RequestParam.class) != null ;
    }
}
