package com.wm.admin.controller;

import cn.hutool.crypto.SecureUtil;
import com.wm.admin.vo.PageVO;
import com.wm.admin.domain.Result;
import com.wm.admin.domain.User;
import com.wm.admin.service.UserService;
import com.wm.admin.util.MySpring;
import org.mvc.MvcFile;
import org.mvc.annotations.Controller;
import org.mvc.annotations.RequestBody;
import org.mvc.annotations.RequestMapping;
import org.mvc.annotations.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Security;
import java.util.*;

@Controller
public class UserController {
    private UserService serv = MySpring.getBean(UserService.class);

    @RequestMapping("/login")
    @RequestBody
    public Result login(@RequestParam("uname") String uname,
                        @RequestParam("upass") String upass,
                        @RequestParam("vercode") String vercode,
                        @RequestParam("remember") String remember,
                        HttpSession session,
                        HttpServletRequest req,
                        HttpServletResponse resp
    ) {
        //�����֤��
        String code = (String) session.getAttribute("code");
        if (!code.equalsIgnoreCase(vercode)) {
            //"��֤�����"
            return Result.error("��֤�����");
        }

        User user = serv.finByUname(uname);
        if (user == null) {
            return Result.error("�û�������");
        }
        if (!user.getUpass().equals(SecureUtil.md5(upass))) {
            return Result.error("�������");
        }
        if(user.getDelete_flag() == 2){
            return Result.error("���ѱ����ã�����ϵ����Ա��");
        }

        String s = UUID.randomUUID().toString();
        session.setAttribute("user", user);
        executeAutoLogin(remember, user.getUid(), req, resp);
        return Result.success(s);
    }

    private void executeAutoLogin(String remember, long uid, HttpServletRequest req, HttpServletResponse resp) {
        if (remember == null || !"on".equals(remember)) return;
        String token = UUID.randomUUID().toString().replace("-", "");
        //token�ṹ�����Ǻܶ� ��-�ָ� ����ʹ��token+ϵͳ��ǰʱ������ ���� token-System.currentTime
        //�����루token�� ����Ҳ����Я����¼��Ϣ ����Ҫ����
        token += "-" + System.currentTimeMillis();
        Cookie ck = new Cookie("token", token);
        ck.setMaxAge(24);
        resp.addCookie(ck);
        //����¼��Ϣ��������洢��Դ洢����
        //application(����������)
        ServletContext application = req.getServletContext();
        application.setAttribute(token, uid);
    }


    @RequestMapping("/updatePwd")
    @RequestBody
    public Result updatePwd(@RequestParam("opass") String opass,
                            @RequestParam("upass") String upass,
                            @RequestParam("repass") String repass,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!user.getUpass().equals(opass)) {
            return Result.error("ԭ�������");
        }
        if (!upass.equals(repass)) {
            return Result.error("���������벻һ��");
        }
        user.setUpass(upass);
        serv.updateUserPwd(user);
//        session.removeAttribute("user");
//        session.setAttribute("user",user);
        return Result.success();
    }

    @RequestMapping("/user/list")
    @RequestBody
    public PageVO userList(@RequestParam("page") int page,
                         @RequestParam("limit") int rows,
                         @RequestParam("uname") String uname,
                         @RequestParam("phone") String phone,
                           @RequestParam("likeC")String isLikeSelect) {
        Map<String,Object> param = new HashMap<>();
        param.put("page",page);
        param.put("rows",rows);
        param.put("uname",uname);
        param.put("phone",phone);
        if(isLikeSelect!=null&&isLikeSelect.equals("1")){
            param.put("isLikeSelect",isLikeSelect);
        }
        PageVO pageVO = serv.selectByPage(param);
        return pageVO;
    }

    @RequestMapping("/add_u")
    @RequestBody
    public Result addUser(User user,HttpSession session){
        User me = (User)session.getAttribute("user");
        user.setCreate_uid(me.getUid());
        return serv.addUser(user);
    }

    @RequestMapping("/flagDel")
    @RequestBody
    public Result updateUserDeleteFlag(@RequestParam("uids")String uids,@RequestParam("flag")Integer value){
        String[] split = uids.split(",");
        if(value==null){
            //�޸�Ϊȫ��ɾ��
            if(serv.updateUserDeleteFlag(split,null)) {
                return Result.success("ɾ���ɹ�");
            }else{
                return Result.error("ɾ���쳣");
            }
        }else if(split.length==1){
            if(serv.updateUserDeleteFlag(split,String.valueOf(value))) {
                return Result.success("ɾ���ɹ�");
            }else{
                return Result.error("ɾ���쳣");
            }
        }
        return Result.error("ɾ���쳣");
    }

    @RequestMapping("/updateU")//�޸��û�
    @RequestBody
    public Result updateUser(User user,HttpSession session){
        //userȱ��ֵ uid ������ ���� �Լ�������id
        // phone mail �����ظ�
        User me = (User)session.getAttribute("user");
        user.setSex("0".equals(user.getSex())?"��":"Ů");
        user.setUpdate_uid(me.getUid());
        return serv.updateUser(user);
    }

    @RequestMapping("/DBdel")//���ݿ�ɾ������û��ӿ�
    @RequestBody
    public Result deleteMore(@RequestParam("uids")String uids){
        //ɾ��ʱЯ����ǰ�û�id
        String[] split = uids.split(",");
        //��Ϊֱ��ɾ�����ݿ����� ���Բ���Ҫ��¼�޸��� ֱ��ɾ��
            if(serv.deleteDB(split)) {
                return Result.success("ɾ���ɹ�");
            }else{
                return Result.error("ɾ���쳣");
            }
    }

    @RequestMapping("/userInfos")
    @RequestBody
    public Result userInfos(@RequestParam("uid")String uid){
        if(!uid.matches("\\d+")){
            return Result.error("��������");
        }
        User user = serv.selectByID(Long.parseLong(uid));
        if(user!=null){
            return Result.success(user);
        }else{
            return Result.error("δ�ҵ����û�");
        }
    }
}
