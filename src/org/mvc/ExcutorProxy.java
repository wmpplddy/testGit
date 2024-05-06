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
 * װ��ִ�й������漰����������Ŀ����� ��ִ��
 */
public class ExcutorProxy {
    //װ��Ŀ�����ִ������Ҫ����Ϣ
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
     * @paramValues �洢�ַ�������
     * @fileParamValues  �洢�ļ����� �����Ǵ洢��һ��MvcFile[] ÿ��MvcFile�洢���ļ�����Ϣ
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
     * Ŀ����Ϣ����Դ������
     */
    public void execute() throws Exception {
        LinkedList<Interceptor> interceptorList = new LinkedList<>();
        //����prevִ��
        for (AopMappingTag aop:interceptors) {
            Class key = aop.getAopClass();
            Interceptor interceptor = (Interceptor)config.getSingleObject(key);
           try{
               if(interceptor.prev(req,resp,target)){
                   interceptorList.add(interceptor);
                   continue;
               }else{
                    //��ǰ������ǰ��û�гɹ�������ִ����һ�������Ŀ��
                   //����֮ǰִ�й���������������ɺ��ô���
                   //interceptorListװ�ص������� ���Ѿ�ִ�й���������
                   // ����ִ�з���ִ��post����
                  throw new Exception("����"+key+" ������ִ��ʧ�� interceptor run failed ����");
               }
           }catch(Exception e){
                //��ǰ������ǰ��û�гɹ�
               System.out.println("\33[36m"+e+"\33[m");
               while(interceptorList.size()>0){
                   Interceptor lastItem = interceptorList.removeLast();
                   lastItem.post(req,resp,target);
               }
               return;
           }
        }
        //ִ��Ŀ��

        executeTarget();

        //ִ������post
        while(interceptorList.size()>0){
            Interceptor lastItem = interceptorList.removeLast();
            lastItem.post(req,resp,target);
        }
    }

    /**
     * ExecutorProxy���ڲ�����<br/>
     * ִ��Ŀ�� ����controller�ķ�����<br/>
     * ������Щ��Ϣ����mappingTag���� ��<br/>
     * ֻ��Ҫ����Ϳ��Ե��� ����new���������÷��� <br/>
     * ����֮ǰ������Ŀ�귽���������޲η��� �п����Ǵ������б�ķ�����<br/>
     * �����б��Ӧ��������� ���ھ����˿�ܵ����� ���п�������������<br/>
     * ���ִ��Ŀ��ǰ��Ҫ��������Щ����  ��Ҫ�����֣�<br/>
     * &nbsp;1.��ͨ������� �ַ��� ʹ��req.getParameter<br/>
     * &nbsp;2.�ļ��ϴ��������ַ��� �ļ���ʹ���ļ��ϴ��������<br/>
     * &nbsp;3.����<br/>
     * so we need ������ǰ�ռ����� <a href="#paramExecute">�鿴����</a>
     *
     */
    private void executeTarget() throws Exception {
        //�����ռ�
        receiveParameter();
        //������
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
        //��Ҫʹ���ļ��ϴ���� ��ȡ�����Ϣ
        if(ServletFileUpload.isMultipartContent(req)){//�ж��ǲ���һ���ļ��ϴ�����
            //�������ļ��ϴ�����Ĵ����
            //�ļ��ϴ����� �ᴫ��������� string+file
            //����FileUpload���� ���ᴦ���FileItem����
            DiskFileItemFactory factory = new DiskFileItemFactory();//�ļ����󹤳�
            ServletFileUpload upload = new ServletFileUpload(factory); //������ײ��ʹ�ô˹������������fileItem����
            //���˲���������request��δ��
            List<FileItem> fileItems = upload.parseRequest(req);
            //���� ���в�������list������
            for (FileItem item:fileItems) {
                //ÿһ��item����������һ��������� �������ֿ��� һ����String һ�����ļ�
                //��Ҫ�ж�
                if(item.isFormField()){//�ж��ǲ���һ����ͨ�ı�Ԫ�� ��String ��ͨ����
                    String key = item.getName();
                    if(key==null||"".equals(key)){
                        key = item.getFieldName();
                    }
                    String value = item.getString("utf-8");
                    //��ʹÿ��ֻ�ܻ��һ������ Ҳ���ڿ��ܻ�ö��ͬ�����������
                    String[] strings = paramValues.get(key);
                    if(strings==null){
                        paramValues.put(key,new String[]{value});
                    }else{
                        //����׷�� ��Ҫ������
                        strings = Arrays.copyOf(strings, strings.length + 1);
                        strings[strings.length-1] = value;
                        paramValues.put(key,strings);
                    }
                }else{
                    //˵����һ���ļ����͵Ĳ��� �ļ���Ϣ�϶� �������ļ������ļ���С���ļ����ͣ��ļ�����
                    String key = item.getFieldName(); //�õ��˲�����
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    long size = item.getSize();
                    byte[] content = item.get();
                    InputStream is = item.getInputStream();//����ͨ���� ��ӻ���ļ�����
                    //��Ϊ��Ϣ�Ƚ϶� ��Ҫʹ��һ������ �洢�ļ���Ϣ
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
        //�����������ǲ����ļ��ϴ����� �����Դ�����ͨ������� ������ʹ��req.getParameter

        Enumeration<String> parameterNames = req.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String paramName = parameterNames.nextElement();

            String[] parameterV = req.getParameterValues(paramName);
            paramValues.put(paramName,parameterV);
        }
        //�������� �����Ѿ��ռ������

    }

    /**
     * ΪĿ�귽�� ���ΰ�����<br/>
     * ���ս��󶨵��������һ������Object[]
     * @return
     */
    public Object[] bindParamDataForTargetMethod() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /**
         * �󶨲��� ʹ�ò���ģʽ �����޷������Զ����ģʽ �Ҳ�����չ �����Ͽ���ԭ��<br/>
         * ����ģʽ���ṩһ�����Խӿ�<br/>
         * �����б�--->���� ��֧�ֵĴ󲿷����� �洢������Դ���� DefaultParameterSource <br/>
         * ��DefaultParameterSourceʵ����һ���ӿ� ParameterSource �����ṩ�������ݵķ��� ��������Ҫ������Դ��<br/>
         * <br/>
         * �ռ�����ʵ����ParameterBindStrategy�Ĳ�ͬ���� һ�ε��� һ���ҵ�֧�ָò����İ���
         * �͵��ð�����bind���� ���ҳ�����ת�� ���س��� ���˷��������б��һ��������ɰ� ����������ѭ��
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
            //�󶨲������� ��Ҫ��������
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
 * 1.�ռ����в���
 * 2.Ŀ�귽���б�󶨲���
 */

}
