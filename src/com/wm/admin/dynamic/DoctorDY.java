package com.wm.admin.dynamic;

import org.mvc.util.StringUtil;

import java.util.Iterator;
import java.util.Map;

public class DoctorDY {

    //            @RequestParam("page") int page,
//            @RequestParam("limit") int rows,
//            @RequestParam("dname") String dname, //姓名
//            @RequestParam("tel") String tel,//电话
//            @RequestParam("cardid") String  cardid,//身份证
//            @RequestParam("kname") String kname ,//科室名
//            @RequestParam("likeC")String isLikeSelect //是否开启模糊搜索
    public String listPaged(Map<String, Object> param) {
        String sql = "select * from doctor d left join knit k on d.d_kid = k.k_id where 1=1 ";
        Object page = param.get("page");
        Object limit = param.get("limit");
        Object info = param.get("info");
        Object unit = param.get("unit");
        Object o = param.get("likeC");
        //业务层过滤page 和 limit 为空 且不为数字的字符串请求 将page小于1的数调为1
        if (param == null ) {
            return sql;
        }
        int cur  = Integer.parseInt((String)page);
        int lim  =  Integer.parseInt((String)limit);
        int start = (cur-1)*lim;
        int end = start + lim;
        String limitStr = " limit "+start+","+end;
        if(unit==null&&info==null){
            return sql + limitStr;
        }
        if(((String)unit).length()==0&&((String)info).length()==0){
            return sql + limitStr;
        }
        String like = " = ";
        if (o != null && o.equals("1")) {
            like = " like ";
            if (info != null && !StringUtil.isEmpty((String) info) && ((String) info).length() != 0) {
                String infos = (String) info;
                param.put("info", "%" + infos + "%");
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : param.keySet()) {
            if (s.equals("unit")) {
                sb.append(" and ");
                sb.append(" kname ");
                sb.append(" = ");
                sb.append(" #{" + s + "} ");
                continue;
            }
            if (param.containsKey(s) && !s.equals("limit") && !s.equals("page") && !s.equals("likeC")) {
                String infostr = (String) param.get(s);
                if (infostr == null || infostr.length() == 0) {
                    continue;
                }
                String s1 = appendField(like, s);
                sb.append(s1);
            }
        }
        sb.append(limitStr);
        String concat = sql.concat(sb.toString());
        return concat;
    }
    public String listPagedCount(Map<String, Object> param) {
        String sql = "select count(*) from doctor d left join knit k on d.d_kid = k.k_id where 1=1 ";
        Object page = param.get("page");
        Object limit = param.get("limit");
        Object info = param.get("info");
        Object unit = param.get("unit");
        Object o = param.get("likeC");
        //业务层过滤page 和 limit 为空 且不为数字的字符串请求 将page小于1的数调为1
        if (param == null ) {
            return sql;
        }
        if(unit==null&&info==null){
            return sql ;
        }
        if(((String)unit).length()==0&&((String)info).length()==0){
            return sql ;
        }
        String like = " = ";
        if (o != null && o.equals("1")) {
            like = " like ";
            if (info != null && !StringUtil.isEmpty((String) info) && ((String) info).length() != 0) {
                String infos = (String) info;
                param.put("info", "%" + infos + "%");
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : param.keySet()) {
            if (s.equals("unit")) {
                sb.append(" and ");
                sb.append(" kname ");
                sb.append(" = ");
                sb.append(" #{" + s + "} ");
                continue;
            }
            if (param.containsKey(s) && !s.equals("limit") && !s.equals("page") && !s.equals("likeC")) {
                String infostr = (String) param.get(s);
                if (infostr == null || infostr.length() == 0) {
                    continue;
                }
                String s1 = appendField(like, s);
                sb.append(s1);
            }
        }
        String concat = sql.concat(sb.toString());
        return concat;
    }

    private String[] whereFields = new String[]{"dname", "cardid", "tel"};

    private String appendField(String like, String paramName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < whereFields.length; i++) {
            if(i==0){
                sb.append(" and ");
            }else{
                sb.append(" or ");
            }
            sb.append(whereFields[i]);
            sb.append(like);
            sb.append("#{" + paramName + "} ");
        }
        return sb.toString();
    }
}
