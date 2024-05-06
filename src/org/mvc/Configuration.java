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
 * ��ȡ �� �洢 �����������Ϣ
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
            "org.mvc.responseHandler.StringHandler", //��Ӧ�ַ���
            "org.mvc.responseHandler.ObjectJsonHandler", // json��ʽ����
            "org.mvc.responseHandler.ForwardHandler",  // ת��
            "org.mvc.responseHandler.RedirectHandler", // �ض���
            "org.mvc.responseHandler.ModelAndViewHandler"  // ת��+Я������
    };
    private List<ResponseHandlerStrategy> handles = new LinkedList<>();
    public List<ResponseHandlerStrategy> getResponseHandle(){
        return this.handles;
    }


    /**
     * �洢���е�mapping-bean һ�������Ӧһ��mapping-bean
     * ������Ҫ���ٸ���url�ҵ���ӦĿ�� ������ʹ��hashmap
     * map.key��name/url
     * map.value:mapping-info
     */
    private Map<String, MappingTag> mappingTags = new HashMap<>();

    /**
     * ��ʵ������
     */
    private Map<Class,Object> singleBox = new HashMap<>();


    /**
     * key = "java.lang.String[]-java.lang.Integer"  <br/>
     * key ��������������� �� �Ҳ�Ŀ�����͵�һ���ַ������
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
     * �洢�Զ���Ĳ������� δ��������ʱ �����α����ҵ����������İ���<br/>
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
     * ��ȡ�����ļ���Ϣ �����ļ�·����Ϊ���� <br/>
     *                     1.classpath��mvc.xml
     *                     2.f/z/mvc.xml
     */
    public void readXML(String configLocation){
        if(configLocation.startsWith("classpath:")){
            //����classpath·����xml
            new ClasspathXMLReader(this).read(configLocation);
        }else if(configLocation.startsWith("http:")){
            //��ʾ����Զ�̵�xml����
            new RemoteXMLReader(this).read(configLocation);
        }else{
            //��ʾ����System·����xml
            new SystemXMLReader(this).read(configLocation);
        }
    }

    /**
     * ��ȡ����ע����Ϣ
     * @Param packagePathָ����ʹ��ע�������ڵİ�
     */
    public void readAnnotation(String packagePath){
        new AnnotationReader(this).read(packagePath);
    }

    /**
     * ��ʼ��������������
     */
    public void initParameterBinder() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Ŀǰ������������� and ʹ�����ṩ�İ���
        //Ӧ��ʹ��list���ϴ洢�������� ��Ҫ���ǰ����洢˳��
        // ����ʵ�����Ӧ�ÿ��� �Զ������˳�����ǰ�� �����ں���
        // ��ȻҲ����Ϊ��������һ����� order
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
            //֤����ǰurl�Լ���mappingTag�� ���Ѿ����ù���
            //�ظ����ã��׳��쳣
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
