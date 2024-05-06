package org.mvc.config;

import org.mvc.Configuration;
import org.mvc.exceptions.ConfigFileNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SystemXMLReader extends AbstractXMLReader implements ConfigReader{

    public SystemXMLReader(Configuration configuration){
        super(configuration);
    }
    @Override
    public void read(String target) {
        File file = new File(target);
        if(!file.exists()){
            //配置文件不存在
            throw new ConfigFileNotFoundException(target);
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            super.read(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
