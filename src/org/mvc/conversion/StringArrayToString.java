package org.mvc.conversion;

public class StringArrayToString implements ConversionStretagy<String[],String>{

    @Override
    public String convert(String[] source) {
        return source[0];
    }
}
