package com.wm.admin.test;

import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.wm.admin.dao.DocDAO;
import com.wm.admin.dao.UserDAO;
import com.wm.admin.domain.User;
import com.wm.admin.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mvc.annotations.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMain {
    public static void main(String[] args) {
        DocDAO mapper = SqlSessionUtil.getMapper(DocDAO.class);
        Map param = new HashMap();
        param.put("page","1");
        param.put("limit","10");
        param.put("info","all"); // 医生姓名 医生身份证 医生电话 and 科室 比如nb科室下的名称叫刘德华的人
        param.put("unit","内科"); //选中复选框 进行数据刷新 医生科室复选框 科室均不启用模糊搜索
        param.put("likeC","1"); //1启用模糊搜索 2不启用模糊搜索
//        @RequestParam("page") int page,
//        @RequestParam("limit") int rows,
//        @RequestParam("dname") String dname, //姓名
//        @RequestParam("tel") String tel,//电话
//        @RequestParam("cardid") String  cardid,//身份证
//        @RequestParam("kname") String kname ,//科室名
//        @RequestParam("likeC")String isLikeSelect //是否开启模糊搜索
        System.out.println(mapper.findAllDoc(param).size());

//        System.out.println(mapper.selectOne("zhangsan1"));
//        Map<String,Object> param = new HashMap<>();
//        param.put("page","1");
//        param.put("rows","3");
//        param.put("uname","zhangsan2");
//        param.put("phone",null);
//        long l0 = System.currentTimeMillis();
//        List<User> users = mapper.selectByPage(param);
//        long l = System.currentTimeMillis();
//        System.out.println(l-l0+" ms");
//        SqlSession session = SqlSessionUtil.getSession();
//
//        System.out.println((User)session.selectOne("com.wm.admin.dao.UserDAO.selectOne","zhangsan1"));
//        System.out.println("12a3".matches("\\d+"));
//        String china = PinyinUtil.getPinyin("", "");
//        System.out.println(china);
        String af = "";
        af = af.replace(" ","");
        af = af.replace("\n","");
        System.out.println(af);

    }
}
