package org.mvc.conversion;

public class StringArrayToIntegerArray implements ConversionStretagy<String[],Integer[]>{
    @Override
    public Integer[] convert(String[] source) {
        Integer[] arr = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            arr[i] = Integer.valueOf(source[i]);
        }
        return arr;
    }
}
