package org.mvc.tag;

import org.mvc.util.StringUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * �洢Mapping��ǩ��Ϣ
 */
public class MappingTag {
    private String name ;
    private String methodStr ;
    //����classStr���mapping����
    private String classStr ;

    //scope ֻ������ֵ singleton/prototype
    private String scope;

    private List<ParamTypeTag> params;
    private Class mappingClass;
    //�����÷�����Ҫ�������� 1.������ ��2.�����б�
    private Method method ;

    public MappingTag(String name, String method, String classStr, List<ParamTypeTag> params) {
        this.name = name;
        this.methodStr = method;
        this.classStr = classStr;
        this.params = params;
    }
    public MappingTag(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodStr() {
        return methodStr;
    }

    public void setMethodStr(String method) {
        this.methodStr = method;
        if(mappingClass!=null && params!=null){
            this.setMethod();
        }
    }

    public String getClassStr() {
        return classStr;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
        try{
            Class<?> aClass = Class.forName(classStr);
            this.setMappingClass(aClass);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public List<ParamTypeTag> getParams() {
        return params;
    }

    public void setParams(List<ParamTypeTag> params) {
        this.params = params;
        if(mappingClass!=null && !StringUtil.isEmpty(methodStr)){
            this.setMethod();
        }

    }

    public Class getMappingClass() {
        return mappingClass;
    }

    public void setMappingClass(Class mappingClass) {
        this.mappingClass = mappingClass;
        if(!StringUtil.isEmpty(methodStr) && params!=null){
            this.setMethod();
        }
    }

    //�����api
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method target_Method) {
        method = target_Method;
    }

    //���ڵ�һ��˽�з��� ����class ��methodname paramtypes �����õķ���
    private void setMethod(){
        //�ڶ���������Ҫparamtag����ȡ������
        Class[] ClassByParams = new Class[params.size()];
            try {
                for (int i = 0; i < ClassByParams.length; i++) {
                    String ParamClassName = params.get(i).getText();
                    Class GenericParamType = typeStrToClass(ParamClassName);
                    ClassByParams[i] = GenericParamType;
                }
                this.method = mappingClass.getMethod(methodStr, ClassByParams);

            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            }

    }
    private Class typeStrToClass(String classname) throws ClassNotFoundException{
        switch (classname){
            case "int":
                return int.class;
            case "double":
                return double.class;
            case "byte":
                return byte.class;
            case "float":
                return float.class;
            case "short":
                return short.class;
            case "boolean":
                return boolean.class;
            case "char":
                return char.class;
            case "long":
                 return long.class;
            default:
                return Class.forName(classname);
        }
    }
}
