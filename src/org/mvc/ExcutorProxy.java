package org.mvc;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.mvc.binding.ParameterBindStrategy;
import org.mvc.responseHandler.ResponseHandlerStrategy;
import org.mvc.tag.AopMappingTag;
import org.mvc.tag.MappingTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 装载执行过程中涉及的切面对象和目标对象 并执行
 */
public class ExcutorProxy {
    //装载目标对象执行所需要的信息
    private MappingTag target ;
    private List<AopMappingTag> interceptors ;
    private Configuration config ;

    private HttpServletRequest req ;
    private HttpServletResponse resp;
    public HttpServletRequest getReq() {
        return req;
    }
    public void setReq(HttpServletRequest req) {
        this.req = req;
    }
    public HttpServletResponse getResp() {
        return resp;
    }
    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }
    public ExcutorProxy(Configuration config) {
        this.config = config;
    }

    /**
     * @paramValues 存储字符串参数
     * @fileParamValues  存储文件参数 具体是存储了一个MvcFile[] 每个MvcFile存储了文件的信息
     */
    private Map<String,String[]> paramValues = new HashMap<>();
    private Map<String,MvcFile[]> fileParamValues = new HashMap<>();

    public MappingTag getTarget() {
        return target;
    }

    public void setTarget(MappingTag target) {
        this.target = target;
    }

    public List<AopMappingTag> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<AopMappingTag> interceptors) {
        this.interceptors = interceptors;
    }

    /**
     * 目标信息都来源于请求
     */
    public void execute() throws Exception {
        LinkedList<Interceptor> interceptorList = new LinkedList<>();
        //切面prev执行
        for (AopMappingTag aop:interceptors) {
            Class key = aop.getAopClass();
            Interceptor interceptor = (Interceptor)config.getSingleObject(key);
           try{
               if(interceptor.prev(req,resp,target)){
                   interceptorList.add(interceptor);
                   continue;
               }else{
                    //当前拦截器前置没有成功，不再执行下一个切面或目标
                   //但是之前执行过的拦截器还需完成后置处理
                   //interceptorList装载的拦截器 是已经执行过的拦截器
                   // 所以执行反向执行post即可
                  throw new Exception("【【"+key+" 拦截器执行失败 interceptor run failed 】】");
               }
           }catch(Exception e){
                //当前拦截器前置没有成功
               System.out.println("\33[36m"+e+"\33[m");
               while(interceptorList.size()>0){
                   Interceptor lastItem = interceptorList.removeLast();
                   lastItem.post(req,resp,target);
               }
               return;
           }
        }
        //执行目标

        executeTarget();

        //执行切面post
        while(interceptorList.size()>0){
            Interceptor lastItem = interceptorList.removeLast();
            lastItem.post(req,resp,target);
        }
    }

    /**
     * ExecutorProxy的内部方法<br/>
     * 执行目标 调用controller的方法，<br/>
     * 但是这些信息都在mappingTag里面 。<br/>
     * 只需要反射就可以调用 反射new对象来调用方法 <br/>
     * 但是之前分析了目标方法不再是无参方法 有可能是带参数列表的方法。<br/>
     * 参数列表对应了请求参数 现在经过了框架的请求 都有框架来处理参数。<br/>
     * 因此执行目标前需要分析有哪些参数  主要就两种：<br/>
     * &nbsp;1.普通请求参数 字符串 使用req.getParameter<br/>
     * &nbsp;2.文件上传参数：字符串 文件，使用文件上传组件接收<br/>
     * &nbsp;3.其他<br/>
     * so we need 绑定数据前收集参数 <a href="#paramExecute">查看解析</a>
     *
     */
    private void executeTarget() throws Exception {
        //数据收集
        receiveParameter();
        //参数绑定
        Object[] objs = bindParamDataForTargetMethod();


        Object singleObject = config.getSingleObject(target.getMappingClass());
        Object result;
        if((result = target.getMethod().invoke(singleObject, objs)) ==null){
            return;
        }
        for (ResponseHandlerStrategy handle: config.getResponseHandle()) {
            if(handle.isSupport(result, target.getMethod())){
                handle.handle(result, target.getMethod(), req,resp);
                break;
            }
        }

    }
    public void receiveParameter() throws FileUploadException, IOException {
        //需要使用文件上传组件 获取相关信息
        if(ServletFileUpload.isMultipartContent(req)){//判断是不是一个文件上传请求
            //这里是文件上传请求的代码块
            //文件上传请求 会传递两类参数 string+file
            //对于FileUpload而言 都会处理成FileItem对象
            DiskFileItemFactory factory = new DiskFileItemFactory();//文件对象工厂
            ServletFileUpload upload = new ServletFileUpload(factory); //此组件底层会使用此工厂将参数组成fileItem对象
            //到此参数还是在request中未变
            List<FileItem> fileItems = upload.parseRequest(req);
            //到此 所有参数都在list集合中
            for (FileItem item:fileItems) {
                //每一个item参数都代表一个请求参数 包含两种可能 一个是String 一个是文件
                //需要判断
                if(item.isFormField()){//判断是不是一个普通的表单元素 即String 普通参数
                    String key = item.getName();
                    if(key==null||"".equals(key)){
                        key = item.getFieldName();
                    }
                    String value = item.getString("utf-8");
                    //即使每次只能获得一个参数 也存在可能获得多个同名参数的情况
                    String[] strings = paramValues.get(key);
                    if(strings==null){
                        paramValues.put(key,new String[]{value});
                    }else{
                        //数组追加 需要新数组
                        strings = Arrays.copyOf(strings, strings.length + 1);
                        strings[strings.length-1] = value;
                        paramValues.put(key,strings);
                    }
                }else{
                    //说明是一个文件类型的参数 文件信息较多 包括：文件名，文件大小，文件类型，文件内容
                    String key = item.getFieldName(); //拿到了参数名
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    long size = item.getSize();
                    byte[] content = item.get();
                    InputStream is = item.getInputStream();//可以通过流 间接获得文件内容
                    //因为信息比较多 需要使用一个对象 存储文件信息
                    MvcFile file = new MvcFile(fileName,contentType,size,content,is);
                    MvcFile[] files = fileParamValues.get(key);
                    if(files==null){
                        fileParamValues.put(key,new MvcFile[]{file});
                    }else{
                        files = Arrays.copyOf(files, files.length + 1);
                        files[files.length-1] = file;
                        fileParamValues.put(key,files);
                    }
                }
            }

        }
        //到这里无论是不是文件上传请求 都可以传递普通请求参数 都可以使用req.getParameter

        Enumeration<String> parameterNames = req.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String paramName = parameterNames.nextElement();

            String[] parameterV = req.getParameterValues(paramName);
            paramValues.put(paramName,parameterV);
        }
        //代码至此 参数已经收集完毕了

    }

    /**
     * 为目标方法 依次绑定数据<br/>
     * 最终将绑定的数据组成一个数组Object[]
     * @return
     */
    public Object[] bindParamDataForTargetMethod() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /**
         * 绑定参数 使用策略模式 否则无法加入自定义绑定模式 且不可拓展 不符合开闭原则<br/>
         * 策略模式：提供一个策略接口<br/>
         * 参数列表：--->数据 将支持的大部分数据 存储在数据源类中 DefaultParameterSource <br/>
         * 且DefaultParameterSource实现了一个接口 ParameterSource 里面提供返回数据的方法 隐藏了主要的数据源类<br/>
         * <br/>
         * 收集所有实现了ParameterBindStrategy的不同绑定器 一次调用 一旦找到支持该参数的绑定器
         * 就调用绑定器的bind方法 查找出数据转型 返回出来 到此方法参数列表的一个参数完成绑定 跳出绑定器的循环
         */
        DefaultParameterSource dataSource = new DefaultParameterSource();
        dataSource.setRequest(req);
        dataSource.setResponse(resp);
        dataSource.setParamValues(paramValues);
        dataSource.setFileParamValues(fileParamValues);
        Method method = target.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] values = new Object[parameters.length];
        for (int i = 0; i < values.length; i++) {
            Parameter parameter = parameters[i];
            //绑定参数数据 需要参数绑定器
            for (ParameterBindStrategy binder:config.getBinders()) {
                if(binder.isSupport(parameter)){
                    values[i] = binder.bind(parameter,dataSource);
                    break;
                }
            }
        }
        return values;
    }

/**
 * 1.收集所有参数
 * 2.目标方法列表绑定参数
 */

}
