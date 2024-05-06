package org.mvc.conversion;

public class StringArrayToLong implements ConversionStretagy<String[],Long>{
    @Override
    public Long convert(String[] source) {
        return Long.valueOf(source[0]);
    }
}
