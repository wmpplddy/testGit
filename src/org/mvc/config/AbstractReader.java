package org.mvc.config;

import org.mvc.Configuration;

public abstract class AbstractReader implements ConfigReader{

    protected Configuration configuration;
    public AbstractReader(Configuration configuration){
        this.configuration = configuration;
    }
}
