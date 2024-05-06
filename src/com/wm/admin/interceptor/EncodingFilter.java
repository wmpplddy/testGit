package com.wm.admin.interceptor;

import org.mvc.Interceptor;
import org.mvc.annotations.InterceptorAspect;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class EncodingFilter extends HttpFilter {
    private String ENCODING ;
    public void init(FilterConfig filterConfig) throws ServletException {
        ENCODING = filterConfig.getInitParameter(ENCODING);
        if(ENCODING==null||"".equals(ENCODING)||"null".equals(ENCODING)){
            ENCODING = "UTF-8";
        }
    }

    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        req.setCharacterEncoding(ENCODING);
        chain.doFilter(req,resp);
    }
}
