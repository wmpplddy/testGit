package org.mvc.config;

import org.mvc.Configuration;
import org.mvc.exceptions.ConfigFileNotFoundException;

import java.io.InputStream;

public class ClasspathXMLReader extends AbstractXMLReader implements ConfigReader{

    public ClasspathXMLReader(Configuration configuration){
        super(configuration);
    }
    @Override
    public void read(String target) {
        //解析配置文件【xml文件】
        //解析完毕需要存储配置信息
        String path = target.replace("classpath:","").trim();
        //如果路径不正确 那么获取流时会发生异常
        try{
            InputStream rs = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            super.read(rs);
        }catch(NullPointerException e){
            throw new ConfigFileNotFoundException(path);
        }
        //需要使用Configuration对象存储信息

    }

}
