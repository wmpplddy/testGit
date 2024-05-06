package org.mvc.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mvc.Configuration;
import org.mvc.tag.*;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * 这个类是Xml读取器的公共实现
 */
public abstract class AbstractXMLReader extends AbstractReader {
    //这里的功能待定。。。。

    public AbstractXMLReader(Configuration configuration){
        super(configuration);
    }

    /**
     * xml文件读写的落地操作 需要dom4j的包 引入两个依赖
     * @param is
     */
    protected void read(InputStream is){
        SAXReader reader = new SAXReader();

        Document mvc_xml = null;
        try {
            mvc_xml = reader.read(is);
            readMapping(mvc_xml);
            readAopMapping(mvc_xml);
            readParameterBinder(mvc_xml);
            readConvertor(mvc_xml);
            //in the future继续解耦
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    /**
     * 读取mapping标签
     */
    protected final void readMapping(Document doc) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Element> list = doc.selectNodes("com/town/mvc/mvc-mapping");
        for (Element mapping:list) {
            String name = mapping.attributeValue("name");
            String classStr = mapping.attributeValue("class");
            String methodStr = mapping.attributeValue("method");
            MappingTag mappingObj = new MappingTag();
            mappingObj.setName(name);
            mappingObj.setClassStr(classStr);
            mappingObj.setMethodStr(methodStr);
            List<ParamTypeTag> ParamTypeTags = new ArrayList<>();
            List<Element> paramTypeList = mapping.selectNodes("param-type");
            for (Element paramTypeDoc: paramTypeList) {
                String ParamKeyStr = paramTypeDoc.attributeValue("name");
                String ParamOriginStr = paramTypeDoc.attributeValue("type");
                String ParamTypeClassNameStr = paramTypeDoc.getText();
                ParamTypeTag paramTag = new ParamTypeTag(ParamKeyStr,ParamOriginStr,ParamTypeClassNameStr);
                ParamTypeTags.add(paramTag);
            }
            mappingObj.setParams(ParamTypeTags);
            configuration.putMapping(mappingObj);
            //mapping对象每完善一个 Config对象都需要储存起来

            Class mappingClass = mappingObj.getMappingClass();
            if(!configuration.isExist(mappingClass)){
                    Object o = mappingClass.getConstructor().newInstance();
                    configuration.putSingleObject(mappingClass,o);
            }


        }
    }

    /**
     * 读取aop标签
     */
    protected void readAopMapping(Document doc) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Element> list = doc.selectNodes("com/town/mvc/aop-mapping");
        for (Element aopTag:list) {
            String classStr = aopTag.attributeValue("class");
            String includeV = aopTag.attributeValue("include");
            String excludeV = aopTag.attributeValue("exclude");
            AopMappingTag aop = new AopMappingTag();
            aop.setClassStr(classStr);
            aop.setInclude(includeV);
            aop.setExclude(excludeV);
            //这个对象需要存储起来（切面信息对象） 需要存储在Configuration中
            configuration.putAopMapping(aop);

//            if(scope=="singleton"){
//
//            }
            Class aopClass = aop.getAopClass();
            if(!configuration.isExist(aopClass)){
                Object o = aopClass.getConstructor().newInstance();
                configuration.putSingleObject(aopClass,o);
            }

        }
    }

    /**
     * 读取用户自定义的参数绑定器
     */
    protected void readParameterBinder(Document doc){
        List<Element> list = doc.selectNodes("param-binder");
        for (Element ele:list) {
            String binderpath = ele.attributeValue("class");
            ParamBinderTag binderInfo = new ParamBinderTag();
            binderInfo.setBinderPath(binderpath);
            configuration.addBinder(binderInfo);
        }
    }
    protected void readConvertor(Document doc){
        List<Element> convers = doc.selectNodes("convertor");
        for (Element con:convers) {
            String classStr = con.attributeValue("class");
            ConvertorTag tag = new ConvertorTag();
            tag.setClassStr(classStr);
            configuration.addConvertor(tag);
        }
    }



}
