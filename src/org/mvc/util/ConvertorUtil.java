package org.mvc.util;

import org.mvc.Configuration;
import org.mvc.conversion.ConversionStretagy;

public class ConvertorUtil {
    private static Configuration config;
    public static void setConfig(Configuration configr){
        if(config!=null){
            return ;
        }
        config = configr;
    }
    public static Object Convert(Object source ,Class type) throws Exception {
        if(source==null){
            return null;
        }
        String L = source.getClass().getTypeName();
        L = L.replace(";","");
        String R ;
        R = getTypeName(type);
        String key = L+"-"+R;
        ConversionStretagy convertor = config.getConvertors(key);
        if(convertor!=null){
             return convertor.convert(source);
        }else{
            return source;
        }
        //java.lang.String[]-java.lang.String
    }

    //将参数转换成目标类型的获取目标类型的string方法
    private static String getTypeName(Class ttype){
        if(int.class == ttype){
            return Integer.class.getTypeName();
        }else if(double.class == ttype){
            return Double.class.getTypeName();
        }else if(long.class == ttype){
            return Long.class.getTypeName();
        }else if(byte.class == ttype){
            return Byte.class.getTypeName();
        }else{
            return ttype.getTypeName();
        }
    }

}
