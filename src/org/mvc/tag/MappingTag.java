package org.mvc.tag;

import org.mvc.util.StringUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 存储Mapping标签信息
 */
public class MappingTag {
    private String name ;
    private String methodStr ;
    //反射classStr获得mapping对象
    private String classStr ;

    //scope 只有俩个值 singleton/prototype
    private String scope;

    private List<ParamTypeTag> params;
    private Class mappingClass;
    //反射获得方法需要两个条件 1.方法名 ，2.参数列表
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

    //对外的api
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method target_Method) {
        method = target_Method;
    }

    //对内的一个私有方法 根据class 和methodname paramtypes 反射获得的方法
    private void setMethod(){
        //第二个参数需要paramtag来获取类数组
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
