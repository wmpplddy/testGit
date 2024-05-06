package com.wm.admin.interceptor;

import com.wm.admin.util.AutoLoginUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证
 */
@WebFilter("/*")
public class LoginFilter extends HttpFilter {
    private static String[] excludes = new String[]{
            "login.jsp","login","verifyCode","exit",
            "timeout.jsp","*.png","*.jpg","*.css","*.js"
            ,"*.eot","*.svg","*.ttf","*.woff","*.woff2","forget.jsp","forget"
            ,"updatePwd.htm","mailTip.jsp","updatePwdJSP","*.webp"};
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String path = req.getServletPath();
        System.out.println(path);
        for (String v : excludes) {
            path = path.startsWith("/")?path.substring(1):path;
            if(v.startsWith("*")){
                v = v.substring(1);
                if(path.endsWith(v)){
                    chain.doFilter(req,resp);
                    return;
                }
            }else{
                if(v.equals(path)){
                    chain.doFilter(req,resp);
                    return;
                }
            }
        }
        Object user = req.getSession().getAttribute("user");
        if(user!=null){
            System.out.println("----->认证通过");
            chain.doFilter(req,resp);
            return;
        }else{
            if(AutoLoginUtil.isAutoLogin(req,resp)){
                chain.doFilter(req,resp);
                return;
            }
            System.out.println("----->认证失败");
            req.getRequestDispatcher("timeout.jsp").forward(req,resp);
            return;
        }

    }
}
