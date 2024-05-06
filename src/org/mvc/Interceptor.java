package org.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    /**
     * @return ��Ŀ��֮ǰִ�� true������ִ�У���һ��������interceptor��Ŀ�꣩
     */
    boolean prev(HttpServletRequest req , HttpServletResponse resp,Object target);

    /**
     * ��Ŀ��֮��ִ��
     */
    default void post(HttpServletRequest req , HttpServletResponse resp ,Object target){
        throw new UnsupportedOperationException();
    }
}
