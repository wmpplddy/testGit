package org.mvc.config;

public interface ConfigReader {
    /**
     * 这是一个读取器
     * @param target <br/>
     *            xml:classpath:mvc.xml
     *               f:/z/mvc.xml
     *               url:http://172.198.0.122:6167
     *
     *           注解： com.controller.TestController
     */
    void read(String target);
}
