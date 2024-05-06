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
        //检测验证码
        String code = (String) session.getAttribute("code");
        if (!code.equalsIgnoreCase(vercode)) {
            //"验证码错误"
            return Result.error("验证码错误");
        }

        User user = serv.finByUname(uname);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!user.getUpass().equals(SecureUtil.md5(upass))) {
            return Result.error("密码错误");
        }
        if(user.getDelete_flag() == 2){
            return Result.error("您已被禁用，请联系管理员。");
        }

        String s = UUID.randomUUID().toString();
        session.setAttribute("user", user);
        executeAutoLogin(remember, user.getUid(), req, resp);
        return Result.success(s);
    }

    private void executeAutoLogin(String remember, long uid, HttpServletRequest req, HttpServletResponse resp) {
        if (remember == null || !"on".equals(remember)) return;
        String token = UUID.randomUUID().toString().replace("-", "");
        //token结构可以是很多 用-分割 这里使用token+系统当前时间生成 即： token-System.currentTime
        //令牌码（token） 本身也可以携带登录信息 不过要加密
        token += "-" + System.currentTimeMillis();
        Cookie ck = new Cookie("token", token);
        ck.setMaxAge(24);
        resp.addCookie(ck);
        //将登录信息与令牌码存储配对存储起来
        //application(服务器缓存)
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
            return Result.error("原密码错误");
        }
        if (!upass.equals(repass)) {
            return Result.error("新密码输入不一致");
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
            //修改为全部删除
            if(serv.updateUserDeleteFlag(split,null)) {
                return Result.success("删除成功");
            }else{
                return Result.error("删除异常");
            }
        }else if(split.length==1){
            if(serv.updateUserDeleteFlag(split,String.valueOf(value))) {
                return Result.success("删除成功");
            }else{
                return Result.error("删除异常");
            }
        }
        return Result.error("删除异常");
    }

    @RequestMapping("/updateU")//修改用户
    @RequestBody
    public Result updateUser(User user,HttpSession session){
        //user缺少值 uid 助记码 密码 以及创建人id
        // phone mail 不能重复
        User me = (User)session.getAttribute("user");
        user.setSex("0".equals(user.getSex())?"男":"女");
        user.setUpdate_uid(me.getUid());
        return serv.updateUser(user);
    }

    @RequestMapping("/DBdel")//数据库删除多个用户接口
    @RequestBody
    public Result deleteMore(@RequestParam("uids")String uids){
        //删除时携带当前用户id
        String[] split = uids.split(",");
        //因为直接删除数据库数据 所以不需要记录修改人 直接删除
            if(serv.deleteDB(split)) {
                return Result.success("删除成功");
            }else{
                return Result.error("删除异常");
            }
    }

    @RequestMapping("/userInfos")
    @RequestBody
    public Result userInfos(@RequestParam("uid")String uid){
        if(!uid.matches("\\d+")){
            return Result.error("参数错误");
        }
        User user = serv.selectByID(Long.parseLong(uid));
        if(user!=null){
            return Result.success(user);
        }else{
            return Result.error("未找到该用户");
        }
    }
}
