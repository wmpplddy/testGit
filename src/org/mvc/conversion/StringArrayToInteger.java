package org.mvc.conversion;

public class StringArrayToInteger implements ConversionStretagy<String[],Integer>{


    @Override
    public Integer convert(String[] source) {
        return Integer.valueOf(source[0]);
    }
}
