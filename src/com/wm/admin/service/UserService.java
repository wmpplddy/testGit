package com.wm.admin.service;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.wm.admin.dao.UserDAO;
import com.wm.admin.domain.Result;
import com.wm.admin.util.FileGenerated;
import com.wm.admin.vo.PageVO;
import com.wm.admin.domain.User;
import com.wm.admin.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private static final String DEL = "2";
    private static final String NDEL = "1";

    private UserDAO userMapper = SqlSessionUtil.getMapper(UserDAO.class);

    public Result addUser(User user) {
        User u = userMapper.selectOneByUname(user.getUname());
        String zjm = PinyinUtil.getPinyin(user.getUname(),"");
        user.setZjm(zjm);
        User u2 = userMapper.selectOneByZjm(user.getZjm());
        if (u != null || u2!=null) {
            return Result.error("uname");
        }
        User p = userMapper.selectOneByPhone(user.getPhone());
        if (p != null) {
            return Result.error("phone");
        }
        User user1 = userMapper.selectOneByMail(user.getMail());
        if (user1 != null) {
            return Result.error("mail");
        }
        user.setSex(user.getSex().equals("0")?"男":"女");
        user.setUpass("123");
        userMapper.addUser(user);
        boolean b = FileGenerated.UserAvatarADD(user.getZjm(), null);
        System.out.println(b==true?"创建文件失败":"创建成功");
        return Result.success();
    }

    public Result updateUser(User user) {
        String zjm = user.getZjm();
        User u = userMapper.selectOne(zjm);
        if (!u.getPhone().equals(user.getPhone())) {
            User p = userMapper.selectOneByPhone(user.getPhone());
            if (p != null) {
                return Result.error("phone");
            }
        }
        if (!u.getMail().equals(user.getMail())) {
            User user1 = userMapper.selectOneByMail(user.getMail());
            if (user1 != null) {
                return Result.error("mail");
            }
        }
        userMapper.UpdateUser(user);
        return Result.success();
    }

    public User finByUname(String uname) {
        return userMapper.selectOne(uname);
    }

    public void updateUserPwd(User user) {
        userMapper.UpdateUserPwd(user);
    }

    public User selectByID(Long uid) {
        return userMapper.selectByID(uid);
    }

    public PageVO selectByPage(Map<String, Object> param) {
        if (param.get("isLikeSelect") != null) {
            String u = (String) param.get("uname");
            if (u != null && !u.isEmpty()) {
                u = "%" + u + "%";
                param.put("uname", u);
            }
            List<User> users = userMapper.selectByPageLike(param);
            int r = userMapper.selectByPageLikeTotal(param);
            return new PageVO(0, "", r, users);
        }
        List<User> users = userMapper.selectByPage(param);
        Integer page = (Integer) param.get("page");
        Integer rows = (Integer) param.get("rows");
        int row = rows;
        int total = userMapper.selectTotal(param);
        int max = (int) (total % row == 0 ? total / row : (total / row + 1));
        max = Math.max(1, max);
        page = Math.min(page, max);
        param.put("page", page);
        return new PageVO(0, "", total, users);
    }

    public boolean updateUserDeleteFlag(String[] uids, String value) {
        if (value == null) {
            for (String uid : uids) {
                userMapper.updateUserDeleteFlag(uid);
            }
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("uid", uids[0]);
            map.put("flag", value);
            userMapper.changeUserDeleteFlag(map);
        }
        return true;
    }

    public boolean deleteDB(String[] uids) {
        for (String uid : uids) {
            User user = userMapper.selectByID(Long.parseLong(uid));
            FileGenerated.deleteUserFile(user.getZjm());
            userMapper.deleteDB(uid);
        }
        return true;
    }

    public List<String> allZJM() {
        return userMapper.allZJM();
    }

    public void updateUserAvaPath(Map<String, String> map) {
        userMapper.updateUserAvaPath(map);
    }
}
