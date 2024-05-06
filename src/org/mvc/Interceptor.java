package org.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    /**
     * @return 在目标之前执行 true可向下执行（下一个拦截器interceptor或目标）
     */
    boolean prev(HttpServletRequest req , HttpServletResponse resp,Object target);

    /**
     * 在目标之后执行
     */
    default void post(HttpServletRequest req , HttpServletResponse resp ,Object target){
        throw new UnsupportedOperationException();
    }
}
