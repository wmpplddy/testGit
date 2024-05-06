package com.wm.admin.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.extra.mail.MailUtil;
import com.wm.admin.domain.Result;
import com.wm.admin.domain.User;
import com.wm.admin.service.UserService;
import com.wm.admin.util.FileGenerated;
import com.wm.admin.util.MySpring;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.mvc.MvcFile;
import org.mvc.annotations.Controller;
import org.mvc.annotations.RequestBody;
import org.mvc.annotations.RequestMapping;
import org.mvc.annotations.RequestParam;
import org.mvc.util.StringUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
public class CommController {
    private UserService serv = MySpring.getBean(UserService.class);
    private static final DES des = SecureUtil.des();
    private static final HashMap<String, String> updatePwdMap = new HashMap<>();

    @RequestMapping("/verifyCode")
    public void VCode(HttpServletResponse resp, HttpSession session) throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 100);
        String code = lineCaptcha.getCode();
        session.setAttribute("code", code);
        OutputStream out = resp.getOutputStream();
        lineCaptcha.write(out);
        out.flush();
        out.close();
    }

    @RequestMapping("/exit")
    public String exit(HttpServletResponse resp, HttpSession session) throws IOException {
        System.out.println("exit!!@!!!!");
        session.invalidate();
        return "login.jsp";
    }

    @RequestMapping("/forget")
    @RequestBody
    public Result forget(@RequestParam("uname") String uname, HttpServletRequest req) {
        if (StringUtil.isEmpty(uname)) {
            return Result.error("�û���Ϊ��");
        }
        User user = serv.finByUname(uname);
        if (user == null) {
            return Result.error("�û�������");
        }
        //�����ҵ����û� ���ڴ��û���Ϣ Ӧ��������ȷ�ϵĲ���
        //�� �����ʼ� ���Ͷ�����֤��
        //����ʹ�÷����ʼ� δ���û�ͨ������ʼ��������� �����޸�����ҳ�� ���Ǹ��û�����ʲô��һ��������
        //������Ӧ��Я���û���Ϣ ��Ϊ���û���Ϣ �����û���Ϣ��Ҫ���� ��������ӷ��� ������Ҫ����
        //���� ��������Ҫ��Ч�ڣ���Ч�����ǿ�����Ϊ�̶� ����5���� ��ô������Ҫ���ݵ�ǰʱ�䣬�Թ������жϳ�ʱ
        //�������⻹��Ҫע��һ������ --> �������������� ����������Ҫ�ṩ����������
        //δ������һ�������ڱ������˿�Ҳ��һ����������Ҳ��һ�� ���ʹ��request����ȡ
//���ӽṹ��ɺ� ��Ҫ�Բ������м��ܣ�ʹ��hutool�����еļ��ܹ���
        String ip = req.getServerName();
        int port = req.getServerPort();
        String root = req.getContextPath();

        String param = user.getUname() + ";" + System.currentTimeMillis();

        //param��������
        param = des.encryptBase64(param);

        System.out.println("�����ɼ�����Ϣ��" + param);
        String href = "http://" + ip + ":" + port + root + "/updatePwd.htm?p=" + param;
        MailUtil.send(user.getMail(), "�һ�����", "<div style='text-align: center;'><p>��������޸����� <a href='" + href + "'>" + href + "</a></p><br>30������Ч</div>", true);
        return Result.success();
    }

    @RequestMapping("/updatePwd.htm")
    public String updatePwdView(@RequestParam("p") String param, HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        //�ж��Ƿ���� + �û���Ϣ����
        if (param == null) {
            return null;
        }
        param = param.replace(" ", "+");
        System.out.println("�ѽ��ռ�����Ϣ" + param);
        String p = des.decryptStr(param);
        String[] split = p.split(";");
        String username = split[0];
        Long time = Long.parseLong(split[1]);
        long l = System.currentTimeMillis();
        if ((l - time) / 1000 >= 60) {
            Set<String> strings = updatePwdMap.keySet();
            for (String key : strings) {
                String s = updatePwdMap.get(key);
                if (s.equals(username)) {
                    updatePwdMap.remove(key);
                    break;
                }
            }
            Cookie[] cookies = req.getCookies();
            for (Cookie c : cookies) {
                if ("ft1".equals(c.getName())) {
                    c.setMaxAge(0);
                    break;
                }
            }
            return "mailTip.jsp?p=" + "�����ѹ��ڣ������·����ʼ�&v=2";
        } else {
            //�����������������֤��Ϣ һ���Ƿ�������Map�����˵�һ�ε�����ɵļ���Ψһ��ʶ�����Ҵ洢��Map���棬
            //ͬʱ���ظ�ǰ�ˣ���������޸ĵ��������������Я����cookie ���ҷ���������ҲҪ������cookie ���û��Я��
            //��ô�޸�ʧ�� ���������û�и�cookie��ֵ ��ô������� �û��Ѿ��޸��������� ��cookie��ֵ��α��� �����޸�ʧ��

            //����Ƿ�ͬһ�û��ڶ��η�������
            Collection<String> values = updatePwdMap.values();
            for (String v : values) {
                if (v.equals(username)) {
                    return "updatePwd.jsp?uname=" + username;
                }
            }

            //��Ӱ�ȫ��ʶ��Ϣ
            addSecureInfo(req, resp, username);
            return "updatePwd.jsp?uname=" + username;
        }
    }

    private void addSecureInfo(HttpServletRequest req, HttpServletResponse resp, String username) {
        String s = UUID.randomUUID().toString();
        String tok = des.encryptBase64(s + ";" + username);
        Cookie c = new Cookie("ft1", tok);
        c.setMaxAge(1000 * 90);
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cok : cookies) {
                if (cok.getName().equals("ft1")) {
                    if (updatePwdMap.containsKey(cok.getValue())) {
                        return;
                    }
                    cok.setMaxAge(0);
                }
            }
        }
        resp.addCookie(c);
        updatePwdMap.put(tok, username);
    }

    @RequestMapping("/updatePwdJSP")
    @RequestBody
    public Result updatePwdJSP(@RequestParam("uname") String username,
                               @RequestParam("upass") String upass,
                               @RequestParam("repass") String repass,
                               HttpServletRequest req,
                               HttpServletResponse resp,
                               HttpSession session) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return Result.error("��ǰ������ȫ�Խϵͣ���Ϊ����ͣ�޸Ĳ���");
        }
        String key = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("ft1")) {
                if (updatePwdMap.containsKey(c.getValue())) {
                    String usname = (String) updatePwdMap.get(c.getValue());
                    if (usname.equals(username)) {
                        key = c.getValue();
                        c.setMaxAge(0);
                        break;
                    }
                    updatePwdMap.remove(c.getValue());
                    return Result.error("��ǰ������ȫ�Խϵͣ��޸�ʧ��");
                } else {
                    return Result.error("��ǰ������ȫ�Խϵͣ��޸�ʧ��");
                }
            }
        }
        if ("".equals(key)) {
            return Result.error("��ǰ������ȫ�Խϵ�,��Ϊ����ͣ�޸ģ����Ժ�����");
        }
        User user = serv.finByUname(username);
        if (user == null) return Result.error("�û�������");
        if (!user.getUname().equals(username)) {
            return Result.error("�û�������");
        }
        if (!upass.equals(repass)) {
            return Result.error("���������벻һ��");
        }
        user.setUpass(upass);
        serv.updateUserPwd(user);
        updatePwdMap.remove(key);
        return Result.success();
    }

    @RequestMapping("/upload_a")//�ϴ�ͷ��
    @RequestBody
    public Result uploadAvatar(@RequestParam("u_zjm") String u_zjm, @RequestParam("file") MvcFile file) throws FileUploadException, UnsupportedEncodingException {
        if(u_zjm==null || file==null){
            return Result.error("�����������Ժ�����");
        }
        if(FileGenerated.UserAvatarADD(u_zjm,file)){
            return Result.success();
        }
        return Result.error("�ϴ�ʧ�ܣ����Ժ�����");
    }

    @RequestMapping("/download_a")//����ͷ��
    @RequestBody
    public void download_a(@RequestParam("uid") String uid,HttpServletResponse resp) throws IOException {//��ȡͷ��
        if (uid == null||!uid.matches("\\d+")) {
            return;
        }
        resp.setCharacterEncoding("UTF-8");
        User user = serv.selectByID(Long.parseLong(uid));
        if (user != null) {
            byte[] content = FileGenerated.GetUserAvatar(user);
            if(content==null){
                return;
            }
            ServletOutputStream out = resp.getOutputStream();
            out.write(content);
            out.flush();
            out.close();
        }
        return;
    }


}
