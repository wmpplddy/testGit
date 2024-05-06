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
            return Result.error("用户名为空");
        }
        User user = serv.finByUname(uname);
        if (user == null) {
            return Result.error("用户不存在");
        }
        //到此找到此用户 基于此用户信息 应该做本人确认的操作
        //如 发送邮件 发送短信验证码
        //这里使用发送邮件 未来用户通过这个邮件进入链接 访问修改密码页面 我们给用户发了什么？一个超链接
        //该链接应该携带用户信息 因为有用户信息 所以用户信息需要加密 随后点击链接访问 我们需要解密
        //并且 此链接需要有效期，有效期我们可以人为固定 比如5分钟 那么链接需要传递当前时间，以供后面判断超时
        //但是在这还需要注意一个问题 --> ！环境是在邮箱 所以我们需要提供完整的链接
        //未来程序不一定部署在本机，端口也不一定，程序名也不一定 因此使用request来获取
//链接结构完成后 需要对参数进行加密，使用hutool工具中的加密功能
        String ip = req.getServerName();
        int port = req.getServerPort();
        String root = req.getContextPath();

        String param = user.getUname() + ";" + System.currentTimeMillis();

        //param参数加密
        param = des.encryptBase64(param);

        System.out.println("已生成加密信息：" + param);
        String href = "http://" + ip + ":" + port + root + "/updatePwd.htm?p=" + param;
        MailUtil.send(user.getMail(), "找回密码", "<div style='text-align: center;'><p>点击链接修改密码 <a href='" + href + "'>" + href + "</a></p><br>30秒内有效</div>", true);
        return Result.success();
    }

    @RequestMapping("/updatePwd.htm")
    public String updatePwdView(@RequestParam("p") String param, HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        //判断是否过期 + 用户信息解密
        if (param == null) {
            return null;
        }
        param = param.replace(" ", "+");
        System.out.println("已接收加密信息" + param);
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
            return "mailTip.jsp?p=" + "链接已过期，请重新发送邮件&v=2";
        } else {
            //在这里添加了两个认证信息 一个是服务器的Map缓存了第一次点击生成的加密唯一标识，并且存储在Map缓存，
            //同时返回给前端，发送最后修改的请求浏览器必须携带该cookie 并且服务器缓存也要包含该cookie 如果没有携带
            //那么修改失败 如果服务器没有该cookie的值 那么两种情况 用户已经修改完了密码 该cookie的值是伪造的 返回修改失败

            //检查是否同一用户第二次发送请求
            Collection<String> values = updatePwdMap.values();
            for (String v : values) {
                if (v.equals(username)) {
                    return "updatePwd.jsp?uname=" + username;
                }
            }

            //添加安全标识信息
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
            return Result.error("当前环境安全性较低，已为您暂停修改操作");
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
                    return Result.error("当前环境安全性较低，修改失败");
                } else {
                    return Result.error("当前环境安全性较低，修改失败");
                }
            }
        }
        if ("".equals(key)) {
            return Result.error("当前环境安全性较低,已为您暂停修改，请稍后再试");
        }
        User user = serv.finByUname(username);
        if (user == null) return Result.error("用户名错误");
        if (!user.getUname().equals(username)) {
            return Result.error("用户名错误");
        }
        if (!upass.equals(repass)) {
            return Result.error("新密码输入不一致");
        }
        user.setUpass(upass);
        serv.updateUserPwd(user);
        updatePwdMap.remove(key);
        return Result.success();
    }

    @RequestMapping("/upload_a")//上传头像
    @RequestBody
    public Result uploadAvatar(@RequestParam("u_zjm") String u_zjm, @RequestParam("file") MvcFile file) throws FileUploadException, UnsupportedEncodingException {
        if(u_zjm==null || file==null){
            return Result.error("参数错误，请稍后再试");
        }
        if(FileGenerated.UserAvatarADD(u_zjm,file)){
            return Result.success();
        }
        return Result.error("上传失败，请稍后再试");
    }

    @RequestMapping("/download_a")//下载头像
    @RequestBody
    public void download_a(@RequestParam("uid") String uid,HttpServletResponse resp) throws IOException {//获取头像
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
