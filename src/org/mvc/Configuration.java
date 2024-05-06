package org.mvc;

import org.mvc.binding.EntityParameterBinder;
import org.mvc.binding.NormalParameterBinder;
import org.mvc.binding.ParameterBindStrategy;
import org.mvc.binding.ServletParameterBinder;
import org.mvc.config.AnnotationReader;
import org.mvc.config.ClasspathXMLReader;
import org.mvc.config.RemoteXMLReader;
import org.mvc.config.SystemXMLReader;
import org.mvc.conversion.ConversionStretagy;
import org.mvc.exceptions.RepeatMappingTagException;
import org.mvc.responseHandler.ResponseHandlerStrategy;
import org.mvc.tag.AopMappingTag;
import org.mvc.tag.ConvertorTag;
import org.mvc.tag.MappingTag;
import org.mvc.tag.ParamBinderTag;
import org.mvc.util.ConvertorUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 读取 、 存储 、获得配置信息
 */
public class Configuration {
    private List<AopMappingTag> AOP_TAG = new ArrayList<>();
    private static final String[] INNER_CONVERTOR_CLASS = new String[]{
            "org.mvc.conversion.FileArrayToFile",
            "org.mvc.conversion.StringArrayToDouble",
            "org.mvc.conversion.StringArrayToIntArray",
            "org.mvc.conversion.StringArrayToInteger",
            "org.mvc.conversion.StringArrayToIntegerArray",
            "org.mvc.conversion.StringArrayToLong",
            "org.mvc.conversion.StringArrayToString"
    };

    private static final String[] INNER_RESPONSE_HANDLER = new String[]{
            "org.mvc.responseHandler.StringHandler", //响应字符串
            "org.mvc.responseHandler.ObjectJsonHandler", // json格式对象
            "org.mvc.responseHandler.ForwardHandler",  // 转发
            "org.mvc.responseHandler.RedirectHandler", // 重定向
            "org.mvc.responseHandler.ModelAndViewHandler"  // 转发+携带数据
    };
    private List<ResponseHandlerStrategy> handles = new LinkedList<>();
    public List<ResponseHandlerStrategy> getResponseHandle(){
        return this.handles;
    }


    /**
     * 存储所有的mapping-bean 一个请求对应一个mapping-bean
     * 所以需要快速根据url找到对应目标 这里先使用hashmap
     * map.key：name/url
     * map.value:mapping-info
     */
    private Map<String, MappingTag> mappingTags = new HashMap<>();

    /**
     * 单实例盒子
     */
    private Map<Class,Object> singleBox = new HashMap<>();


    /**
     * key = "java.lang.String[]-java.lang.Integer"  <br/>
     * key 就是左侧数据类型 与 右侧目标类型的一个字符串组合
     */
    private Map<String, ConversionStretagy> convertors = new HashMap<>();

    public List<ConvertorTag> convertorTags = new LinkedList<>();

    public void addConvertor(ConvertorTag tag){
        convertorTags.add(tag);
    }
    public ConversionStretagy getConvertors(String m) {
        return convertors.get(m);
    }
    public void setConvertors(Map<String, ConversionStretagy> convertors) {
        this.convertors = convertors;
    }



    /**
     * 存储自定义的参数绑定器 未来参数绑定时 会依次遍历找到符合条件的绑定器<br/>
     */
    private List<ParamBinderTag> paramBinderTags = new LinkedList<>();

    private List<ParameterBindStrategy> binders = new LinkedList<>();
    public void addBinder(ParamBinderTag tag){
        paramBinderTags.add(tag);
    }


    public void putSingleObject(Class key ,Object value){
        singleBox.put(key,value);
    }
    public boolean isExist(Class key){
        return singleBox.containsKey(key);
    }
    public Object getSingleObject(Class key){
        return singleBox.get(key);
    }

    /**
     * 读取配置文件信息 配置文件路径分为两种 <br/>
     *                     1.classpath：mvc.xml
     *                     2.f/z/mvc.xml
     */
    public void readXML(String configLocation){
        if(configLocation.startsWith("classpath:")){
            //基于classpath路径的xml
            new ClasspathXMLReader(this).read(configLocation);
        }else if(configLocation.startsWith("http:")){
            //表示基于远程的xml配置
            new RemoteXMLReader(this).read(configLocation);
        }else{
            //表示基于System路径的xml
            new SystemXMLReader(this).read(configLocation);
        }
    }

    /**
     * 读取配置注解信息
     * @Param packagePath指定了使用注解类所在的包
     */
    public void readAnnotation(String packagePath){
        new AnnotationReader(this).read(packagePath);
    }

    /**
     * 初始化参数绑定器对象
     */
    public void initParameterBinder() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //目前框架有三个绑定器 and 使用者提供的绑定器
        //应该使用list集合存储绑定器对象 需要考虑绑定器存储顺序
        // 其中实体绑定器应该靠后 自定义绑定器顺序放在前面 内置在后面
        // 当然也可以为绑定器增加一个序号 order
        for (ParamBinderTag binderTag:paramBinderTags) {
            Class c = binderTag.getBinderClass();
            ParameterBindStrategy binder = (ParameterBindStrategy)c.getConstructor().newInstance();
            binders.add(binder);
        }
        binders.add(new NormalParameterBinder());
        binders.add(new ServletParameterBinder());
        binders.add(new EntityParameterBinder());
    }
    public void initConvertor() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (String innerConvertorClass:INNER_CONVERTOR_CLASS){
            Class cls = Class.forName(innerConvertorClass);
            ConversionStretagy o = (ConversionStretagy)cls.getConstructor().newInstance();
            String key = getConvertorKey(cls);
            //java.lang.String[]-java.lang.String
            convertors.put(key,o);
        }
        for (ConvertorTag tag:convertorTags) {
            Class cls = tag.getCls();
            ConversionStretagy o = (ConversionStretagy) cls.getConstructor().newInstance();
            String key = getConvertorKey(cls);
            convertors.put(key,o);
        }
        ConvertorUtil.setConfig(this);
    }
    public void initHandles() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (String clsStr:INNER_RESPONSE_HANDLER) {
            ResponseHandlerStrategy o = (ResponseHandlerStrategy)Class.forName(clsStr).getConstructor().newInstance();
            this.handles.add(o);
        }
    }

    private String getConvertorKey(Class conversion){
        Type[] interfaces = conversion.getGenericInterfaces();
        for (Type interf:interfaces) {
            if(interf.getTypeName().startsWith(ConversionStretagy.class.getTypeName())){
                String name = interf.getTypeName();
                int i = name.indexOf("<");
                int i1 = name.indexOf(">");
                String substring = interf.getTypeName().substring(i + 1, i1);
                substring = substring.replace(" ","");
                substring = substring.replace(",","-");
                return substring;
            }
        }
        return null;
    }
    public void putAopMapping(AopMappingTag aop_tag){
        this.AOP_TAG.add(aop_tag);
    }
    public void putMapping(MappingTag mapping){
        String key = mapping.getName();
        if(mappingTags.get(key)!=null){
            //证明当前url以及有mappingTag了 即已经配置过了
            //重复配置，抛出异常
            throw new RepeatMappingTagException(key);
        }
        this.mappingTags.put(mapping.getName(),mapping);
    }
    public List<ParameterBindStrategy> getBinders() {
        return binders;
    }


    public ExcutorProxy getExecuteProxy(String name){
        ExcutorProxy proxy ;
        MappingTag mappingTag = mappingTags.get(name);
        if(mappingTag==null){
            return null;
        }
        proxy = new ExcutorProxy(this);
        proxy.setTarget(mappingTag);

        List<AopMappingTag> ceptorTags = new ArrayList<>();
        for (AopMappingTag aop: AOP_TAG) {
           if(aop.isSurpport(name)){
               ceptorTags.add(aop);
           }
        }
        proxy.setInterceptors(ceptorTags);
        return proxy;
    }

}
