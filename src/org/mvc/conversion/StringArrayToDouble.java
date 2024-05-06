package org.mvc.conversion;

public class StringArrayToDouble implements ConversionStretagy<String[],Double>{


    @Override
    public Double convert(String[] source) {
        return Double.valueOf(source[0]);
    }
}
