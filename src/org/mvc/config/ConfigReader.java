package org.mvc.config;

public interface ConfigReader {
    /**
     * ����һ����ȡ��
     * @param target <br/>
     *            xml:classpath:mvc.xml
     *               f:/z/mvc.xml
     *               url:http://172.198.0.122:6167
     *
     *           ע�⣺ com.controller.TestController
     */
    void read(String target);
}
