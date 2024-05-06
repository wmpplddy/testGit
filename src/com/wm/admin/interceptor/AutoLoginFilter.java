package com.wm.admin.interceptor;

import com.wm.admin.domain.User;
import com.wm.admin.util.AutoLoginUtil;
import com.wm.admin.util.FileGenerated;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/login.jsp","/login"})
public class AutoLoginFilter extends HttpFilter {
    public void init() throws ServletException {
        FileGenerated.GenerateUserInfo();
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        if(AutoLoginUtil.isAutoLogin(req,resp)){
            req.getRequestDispatcher("/main.jsp").forward(req,resp);
            return;
        }
        chain.doFilter(req,resp);
        return;
    }
}
