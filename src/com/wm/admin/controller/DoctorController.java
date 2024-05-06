package com.wm.admin.controller;

import com.wm.admin.service.DoctorService;
import com.wm.admin.util.MySpring;
import com.wm.admin.vo.PageVO;
import org.mvc.annotations.Controller;
import org.mvc.annotations.RequestMapping;
import org.mvc.annotations.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DoctorController {
    private DoctorService serv = MySpring.getBean(DoctorService.class);


    @RequestMapping("/allDoctor")
    public PageVO findAllDoctor(
            @RequestParam("page") int page,
            @RequestParam("limit") int rows,
            @RequestParam("info") String docInfo, //���� ���֤ �绰 �Ա�
            @RequestParam("unit") String unitName ,//������
            @RequestParam("likeC")String isLikeSelect //�Ƿ���ģ������
    ) {
        Map<String,Object> param = new HashMap<>();
        param.put("page",page);
        param.put("limit",rows);
        param.put("info",docInfo);
        param.put("unit",unitName);
        param.put("likeC",isLikeSelect);
        return serv.findAllDoctorByPage(param);
    }
}
