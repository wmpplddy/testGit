package com.wm.admin.service;

import com.wm.admin.dao.DocDAO;
import com.wm.admin.domain.Doctor;
import com.wm.admin.util.MySpring;
import com.wm.admin.util.SqlSessionUtil;
import com.wm.admin.vo.PageVO;

import java.util.List;
import java.util.Map;

public class DoctorService {
    private DocDAO dao = SqlSessionUtil.getMapper(DocDAO.class);
    public PageVO findAllDoctorByPage(Map<String,Object> param){
        Object page = param.get("page");
        Object limit = param.get("limit");
        if(page==null||limit==null||!((String)page).matches("\\d+")||!((String)page).matches("\\d+")){
            return new PageVO(200,"«Î«Û∏Ò Ω¥ÌŒÛ£°",0,null);
        }
        page = Math.max(1,Integer.parseInt((String)page));
        param.put("page",page);
        List<Doctor> allDoc = dao.findAllDoc(param);
        int allDocCount = dao.findAllDocCount(param);
        return new PageVO(0,"",allDocCount,allDoc);
    }
}
