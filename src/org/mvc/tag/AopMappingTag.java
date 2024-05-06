package org.mvc.tag;

import org.mvc.util.StringUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 存储aop-mapping标签信息
 */
public class AopMappingTag {

    private String classStr;

    private String scope;

    //根据请求的url 在include里是否有 因此需要存储多个url
    private Set<String> include ;
    private Set<String> exclude ;

    private Class aopClass ;

    public void setInclude(String include){
        if(StringUtil.isEmpty(include)){
            return ;
        }
        String[] splits = include.split(",");
        this.include = new HashSet(Arrays.asList(splits));
    }
    public void setExclude(String exclude){
        if (StringUtil.isEmpty(exclude)) {
        return ;
        }
        String[] sp = exclude.split(",");
        this.exclude = new HashSet(Arrays.asList(sp));
    }


    public String getClassStr(){
        return this.classStr;
    }
    public void setClassStr(String classStr){
        this.classStr = classStr;
        try {
            Class<?> aClass = Class.forName(classStr);
            this.setAopClass(aClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Class getAopClass() {
        return aopClass;
    }

    public void setAopClass(Class aopClass) {
        this.aopClass = aopClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
    public boolean isSurpport(String name){
        boolean flagA = false;

        if(include==null || include.size()==0){
            //未配置include
            return false;
        }
        for (String includeStr : include){
            if((flagA = isSurpported(name, includeStr))){
                break;
            }
        }
        if(!flagA){
            //include不包含请求
            return false;
        }
        if(exclude==null||exclude.size()==0){
            return flagA;
        }
        for (String excludeStr : exclude){
            if((flagA = isSurpported(name, excludeStr))){
                break;
            }
        }

        return !flagA;
    }

    private boolean isSurpported(String name , String template){
        if(template.equals(name)){
            return true;
        }
        if(template.equals("/*")){
            return true;
        }
        if(template.endsWith("/*")){
            String prefix = template.replace("*","");
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        if(template.startsWith("*.")){
            String suffix = template.replace("*", "");
            if(name.endsWith(suffix)) {
               return true;
            }
        }
        return false;
    }


    private boolean qualityEq(String name , String template){
        return name.equals(template);
    }
    private boolean allReq(String template){
        return "/*".equals(template);
    }
    private boolean endAll(String name,String template){
        if(template.endsWith("/*")){
            String prefix = template.replace("*","");
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
    private boolean startAll(String name,String template){
        if(template.startsWith("*.")){
            String suffix = template.replace("*", "");
            if(name.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }


}
