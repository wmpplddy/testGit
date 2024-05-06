package org.mvc.conversion;

import org.mvc.MvcFile;

public class FileArrayToFile implements ConversionStretagy<MvcFile[],MvcFile>{
    public MvcFile convert(MvcFile[] source) {
        return source[0];
    }
}
