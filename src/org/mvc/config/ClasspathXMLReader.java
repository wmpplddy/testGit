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
        //���������ļ���xml�ļ���
        //���������Ҫ�洢������Ϣ
        String path = target.replace("classpath:","").trim();
        //���·������ȷ ��ô��ȡ��ʱ�ᷢ���쳣
        try{
            InputStream rs = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            super.read(rs);
        }catch(NullPointerException e){
            throw new ConfigFileNotFoundException(path);
        }
        //��Ҫʹ��Configuration����洢��Ϣ

    }

}
