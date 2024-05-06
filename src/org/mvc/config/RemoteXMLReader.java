package org.mvc.config;

import org.mvc.Configuration;

public class RemoteXMLReader extends AbstractXMLReader  implements  ConfigReader{

    public RemoteXMLReader(Configuration configuration){
        super(configuration);
    }
    @Override
    public void read(String target) {

    }

}
