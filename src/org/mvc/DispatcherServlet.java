package org.mvc;

import org.apache.commons.fileupload.FileUploadException;
import org.mvc.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * �ռ�����
 * ������һ��HttpServlet
 * core���
 */
public class DispatcherServlet extends HttpServlet {
    private static final String CONFIG_LOCATION_KEY = "configLocation";
    private static final String CONFIG_PACKAGE_KEY = "configPackage";

    private static final Configuration config = new Configuration();
    public void init() throws ServletException {//
        try{
            String location = this.getInitParameter(CONFIG_LOCATION_KEY);
            if(!StringUtil.isEmpty(location)){
                config.readXML(location);
            }
            String configPackage = this.getInitParameter(CONFIG_PACKAGE_KEY);
            if(!StringUtil.isEmpty(configPackage)){
                config.readAnnotation(configPackage);
            }
            //配置读取完毕 初始化参数绑定器
            config.initParameterBinder();
            config.initConvertor();
            config.initHandles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("----------- end ------------");
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getServletPath();
        ExcutorProxy proxy = config.getExecuteProxy(name);
        if (proxy == null) {//
            req.getServletContext().getNamedDispatcher("default").forward(req, resp);
        }else{
            try {
                proxy.setReq(req);
                proxy.setResp(resp);
                proxy.execute();
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("----- invoke end -----");

    }
}
