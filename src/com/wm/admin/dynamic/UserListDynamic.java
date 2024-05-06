package com.wm.admin.dynamic;

import java.util.Map;

public class UserListDynamic {

    public String dynamicList(Map<String, Object> param) {
//        param.put("page",page);
//        param.put("rows",rows);
//        param.put("uname",uname);
//        param.put("phone",phone);
        String sql = "select u.*,ifnull((select uname from t_user where uid = u.create_uid),'系统管理员') as create_uname,ifnull((select uname from t_user where uid = u.update_uid),'') as update_uname from t_user u where 1=1";
        if (param == null) return sql;
        Object p = param.get("page");
        Object r = param.get("rows");
        Object u = param.get("uname");
        Object t = param.get("phone");
        if (p == null || r == null) {
            return sql;
        }
        int page = (Integer) p;
        page = page <= 0 ? 1 : page;
        int rows = (Integer) r;
        int start = (page - 1) * rows;
        String limit = " limit " + start + "," + rows;

        StringBuilder where = new StringBuilder("");
        StringBuilder sb = new StringBuilder(sql);
        if (u != null && !((String) u).isEmpty()) {
            where.append(" and u.uname=#{uname} or u.zjm=#{uname} or u.trueName = #{uname} or u.mail=#{uname} or u.phone = #{uname}");
        }
        if (t != null && !((String) t).isEmpty()) {
            if (where.isEmpty()) {
                where.append(" and sex=#{phone} or age = #{phone}");
            } else {
                where.append(" or sex=#{phone} or age = #{phone}");
            }
        }
        sb.append(where);
        sb.append(limit);
        return sb.toString();
    }

    public String dynamicListTotalCount(Map<String, Object> param) {
//        param.put("page",page);
//        param.put("rows",rows);
//        param.put("uname",uname);
//        param.put("phone",phone);
        String sql = "select count(*) from t_user u where 1=1";
        if (param == null) return sql;
        Object u = param.get("uname");
        Object t = param.get("phone");
        StringBuilder where = new StringBuilder("");
        StringBuilder sb = new StringBuilder(sql);
        if (u != null && !((String) u).isEmpty()) {
            where.append(" and u.uname=#{uname} or u.zjm=#{uname} or u.trueName = #{uname} or u.mail=#{uname} or u.phone = #{uname}");
        }
        if (t != null && !((String) t).isEmpty()) {
            if (where.isEmpty()) {
                where.append(" and sex=#{phone} or age = #{phone}");
            } else {
                where.append(" or sex=#{phone} or age = #{phone}");
            }
        }
        sb.append(where);
        return sb.toString();
    }

    public String selectByPageLikeTotal12(Map<String, Object> param) {
        String sql = "select count(*) from t_user u where 1=1";
        if (param == null) return sql;
        Object u = param.get("uname");
        Object t = param.get("phone");
        StringBuilder where = new StringBuilder("");
        StringBuilder sb = new StringBuilder(sql);
        if (u != null && !((String) u).isEmpty()) {
            where.append(" and u.uname like #{uname} or u.zjm like #{uname} or u.trueName like #{uname} or u.mail like #{uname} or u.phone like #{uname}");
        }
        if (t != null && !((String) t).isEmpty()) {
            if (where.isEmpty()) {
                where.append(" and sex=#{phone} or age = #{phone}");
            } else {
                where.append(" or sex=#{phone} or age = #{phone}");
            }
        }
        sb.append(where);
        return sb.toString();
    }

    public String selectByPageLike1(Map<String, Object> param) {
        String sql = "select u.*,ifnull((select uname from t_user where uid = u.create_uid),'系统管理员') as create_uname,ifnull((select uname from t_user where uid = u.update_uid),'') as update_uname from t_user u where 1=1 ";
        if (param == null) return sql;
        Object p = param.get("page");
        Object r = param.get("rows");
        Object u = param.get("uname");
        Object t = param.get("phone");
        if (p == null || r == null) {
            return sql;
        }
        int page = (Integer) p;
        page = page <= 0 ? 1 : page;
        int rows = (Integer) r;
        int start = (page - 1) * rows;
        String limit = " limit " + start + "," + rows;
        StringBuilder where = new StringBuilder("");
        StringBuilder sb = new StringBuilder(sql);
        if (u != null && !((String) u).isEmpty()) {
            where.append(" and u.uname like #{uname} or u.zjm like #{uname} or u.trueName like #{uname} or u.mail like #{uname} or u.phone like #{uname}");
        }
        if (t != null && !((String) t).isEmpty()) {
            if (where.isEmpty()) {
                where.append(" and sex=#{phone} or age = #{phone}");
            } else {
                where.append(" or sex=#{phone} or age = #{phone}");
            }
        }
        sb.append(where);
        sb.append(limit);
        return sb.toString();
    }
}
