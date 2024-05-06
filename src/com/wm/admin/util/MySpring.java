package com.wm.admin.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MySpring {
    private static final HashMap<Class,Object> beanMap = new HashMap<>();
    public static <T>T getBean(Class cls){
        Object o = beanMap.get(cls);
        if(o==null){
            synchronized (MySpring.class){
                if (o==null){
                    try {
                        o = cls.getConstructor().newInstance();
                        beanMap.put(cls,o);
                    } catch (InstantiationException e) {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return (T)o;
    }
}
