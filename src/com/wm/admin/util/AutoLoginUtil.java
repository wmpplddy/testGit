package com.wm.admin.util;

import com.wm.admin.domain.User;
import com.wm.admin.service.UserService;
import com.wm.admin.util.MySpring;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoLoginUtil {
    private static UserService serv = MySpring.getBean(UserService.class);
    public static boolean isAutoLogin(HttpServletRequest req, HttpServletResponse resp){
        Cookie[] cookies = req.getCookies();
        if(cookies==null){
            return false;
        }
        for (Cookie c: cookies) {
            if(c.getName().equals("token")){
                String reqToken = c.getValue();
                Long time = Long.parseLong(reqToken.split("-")[1]);
                Long uid = (Long)req.getServletContext().getAttribute(reqToken);
                if(uid==null){
                    //û�и���token��Ϣ�ڳ������ҵ�token 1.α��token  2.���������� ������Ϣ��ʧ ��¼��Ϣ����
                    resp.setHeader("auto-login-info","auto login invalid,tokenID invalid");
                    return false;
                }
                //�ҵ��˵�¼��Ϣ ���迼�ǵ�ǰʱ��
                long nowTime = System.currentTimeMillis();
                long day = (nowTime-time)/1000/60/60/24;
                if(day>7){
                    resp.setHeader("auto-login-info","auto login invalid,tokenID expire");
                    return false;
                }
                //Ҫ����¼��Ϣ����session
                User user = serv.selectByID(uid);
                if(user==null){
                    resp.setHeader("auto-login-info","auto login fail,user doesn't exist");
                    return false;
                }
                req.getSession().setAttribute("user",user);
                return true;
            }
        }
        return false;
    }
}
