package org.mvc.binding;

import org.mvc.ParameterSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public interface ParameterBindStrategy {

    /**
     * ��paramSource�л��parameter�����������Ҫ�Ĳ���ֵ ��Ŀǰ����3��<br/>
     * 1.  1:1�ļ�ֵ��<br/>
     * 2.  1:n���������<br/>
     * 3.  servlet��ض�����������<br/>
     * @param parameter
     * @param source
     * @return
     */
    Object bind(Parameter parameter, ParameterSource source) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    /**
     * �жϵ�ǰ������� �Ƿ�֧�ֵ�ǰ�������<br/>
     * ʹ������󶨲����ܷ�Ϊ��ǰ��������ҵ���Ӧ�Ĳ���ֵ<br/>
     *
     * @return
     */
    boolean isSupport(Parameter parameter);
}
