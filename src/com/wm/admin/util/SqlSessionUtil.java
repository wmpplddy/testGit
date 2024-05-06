package com.wm.admin.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;


//¹¤³§
public class SqlSessionUtil {
    private static final SqlSessionFactory web_hisFactory;
    static{
        web_hisFactory = new SqlSessionFactoryBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.xml"));
    }
    public static SqlSession getSession(){return getSession(false);}
    public static SqlSession getSession(boolean isAutoCommit){
        return web_hisFactory.openSession(isAutoCommit);
    }
    public static <T>T getMapper(Class<T> mapperClass){
        return SqlSessionManager.newInstance(web_hisFactory).getMapper(mapperClass);
    }
}
