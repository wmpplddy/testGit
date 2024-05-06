package org.mvc.conversion;

public class StringArrayToIntArray implements ConversionStretagy<String[],int[]>{
    @Override
    public int[] convert(String[] source) {
        int[] intArr = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            intArr[i] = Integer.parseInt(source[i]);
        }
        return intArr;
    }
}
