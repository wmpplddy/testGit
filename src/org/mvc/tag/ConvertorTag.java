package org.mvc.tag;

import org.mvc.util.StringUtil;

public class ConvertorTag {

    public Class cls ;
    public String classStr ;

    public ConvertorTag() {

    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        if(!StringUtil.isEmpty(classStr)){
            this.classStr = classStr;
            Class<?> aClass = null;
            try {
                 aClass = Class.forName(classStr);
            } catch (ClassNotFoundException e) {
                System.out.println("\33[32m未找到Convertor类【类型转换器】 ---> "+classStr+" <--- \33[m");
                System.out.println(e);
            }
            this.setCls(aClass);
        }
    }
}
