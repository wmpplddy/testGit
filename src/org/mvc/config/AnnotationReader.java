package org.mvc.config;

import org.mvc.Configuration;
import org.mvc.annotations.Controller;
import org.mvc.annotations.InterceptorAspect;
import org.mvc.annotations.RequestMapping;
import org.mvc.binding.ParameterBinder;
import org.mvc.conversion.Convertor;
import org.mvc.tag.AopMappingTag;
import org.mvc.tag.ConvertorTag;
import org.mvc.tag.MappingTag;
import org.mvc.tag.ParamBinderTag;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ����ע��Ķ�ȡ�� : com.controller.TestController
 */
public class AnnotationReader extends AbstractReader implements ConfigReader{

    public AnnotationReader(Configuration configuration){
        super(configuration);
    }
    @Override
    public void read(String packagePath){
        String s = packagePath.replaceAll("[\\r\\n]", "");
        String[] packs = s.split(",");
        for (String cval:packs) {
            String cpath = cval.trim();
            this.packageScan(cpath);
        }

    }
    public void packageScan(String path) {
        // com.web.controller
        // com/web/controller
        String Dirpath = path.replaceAll("\\.", "/");
//        System.out.println("\33[34mDirPath ------ :"+Dirpath+"\33[m");
        String path1 = "";
        try {
            path1 = Thread.currentThread().getContextClassLoader().getResource(Dirpath).getPath();

        } catch (NullPointerException e) {
            //�����쳣 ���ǿɽ���
            System.out.println("\33[31m|------------------------|\33[m");
            System.out.println("\33[31m| package is not exist!!!| reference you config with ConfigPackage:[" + path + "] \33[m");
            System.out.println("\33[31m|------------------------|\33[m");
        }
        File p = new File(path1);
        File[] files = p.listFiles();
        for (File child:files) {
            String name = child.getName();
            if(child.isDirectory()){
                this.packageScan(path+"."+name);
            }else{
                //��һ���ļ� �������ͨ�ļ���
                if(name.endsWith(".class")){
                    String Cname = name.replace(".class", "");
                    String classname = path+"."+Cname;
                    this.readClass(classname);
                }
            }
        }
    }


    private void readClass(String classname){
        // com.web.controller.TestController
        try{
            //��������� controller interceptor
            Class<?> a = Class.forName(classname);
            Annotation annotation ;
            if((annotation = a.getAnnotation(Controller.class))!=null){
                //������һ��controller��
                this.readController(a);
            }else if((annotation = a.getAnnotation(InterceptorAspect.class))!=null){
                this.readInterceptor(a);
            }else if((annotation = a.getAnnotation(ParameterBinder.class))!=null){
                this.readBinder(a);
            }else if( (annotation = a.getAnnotation(Convertor.class)) != null){
                this.readConvertor(a);
            }else{
                //������Ǹ�����Ҫɨ�账�����[δ��עInterceptorAspectע��||δ��עController]

            }
        }catch(ClassNotFoundException e){
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
    private void readController(Class ControllerClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Method[] methods = ControllerClass.getMethods();
        for (Method m:methods) {
            RequestMapping requestMappingAnnotation = m.getAnnotation(RequestMapping.class);
            if(requestMappingAnnotation==null){
               continue;
            }
            String nameStr = requestMappingAnnotation.value();
            MappingTag mappingTag = new MappingTag();
            mappingTag.setName(nameStr);
            mappingTag.setMappingClass(ControllerClass);
            mappingTag.setMethod(m);
            configuration.putMapping(mappingTag);

            Class mappingClass = mappingTag.getMappingClass();
            if(!configuration.isExist(mappingClass)){
                Object o = mappingClass.getConstructor().newInstance();
                configuration.putSingleObject(mappingClass,o);
            }
        }
    }
    private void readInterceptor(Class InterceptorClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        InterceptorAspect an = (InterceptorAspect) InterceptorClass.getAnnotation(InterceptorAspect.class);
        String includeStr = an.include();
        String excludeStr = an.exclude();
        AopMappingTag aop = new AopMappingTag();
        aop.setAopClass(InterceptorClass);
        aop.setInclude(includeStr);
        aop.setExclude(excludeStr);
        //���������Ҫ�洢������������Ϣ���� ��Ҫ�洢��Configuration��
        configuration.putAopMapping(aop);

        Class aopCls = aop.getAopClass();
        if(!configuration.isExist(aopCls)){
            Object o = aopCls.getConstructor().newInstance();
            configuration.putSingleObject(aopCls,o);
        }
    }
    private void readBinder(Class BinderClass){
        ParamBinderTag tag = new ParamBinderTag();
        tag.setBinderClass(BinderClass);
        configuration.addBinder(tag);
    }
    private void readConvertor(Class convertor){
        ConvertorTag tag = new ConvertorTag();
        tag.setCls(convertor);
        configuration.addConvertor(tag);
    }

}
