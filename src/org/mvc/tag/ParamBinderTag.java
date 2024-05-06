package org.mvc.tag;

import org.mvc.util.StringUtil;

public class ParamBinderTag {
    private String binderPath;
    private Class binderClass;

    public String getBinderPath() {
        return binderPath;
    }

    public void setBinderPath(String binderPath) {
        this.binderPath = binderPath;
        if(!StringUtil.isEmpty(binderPath)){
            try {
                Class<?> aClass = Class.forName(binderPath);
                setBinderClass(aClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public Class getBinderClass() {
        return binderClass;
    }

    public void setBinderClass(Class binderClass) {
        this.binderClass = binderClass;
    }
}
