package com.wm.admin.dao;

import com.wm.admin.domain.Role;
import org.apache.ibatis.annotations.Insert;

import java.util.List;
import java.util.Map;

public interface RoleDAO {

    /**
     * 过滤分页查询前
     * 获得过滤分页的记录总数 按过滤条件进行查询
     * 需传递过滤条件：rname (String,null able)
     * @Param rname
     */
    Long total(String rname);


    /**
     * 过滤分页查询显示
     * 过滤+分页条件 -->Map
     * 过滤条件：rname(String,null able)
     * 分页条件：start(int,not null),length(int,not null)
     * @param map {start,length,rname}
     * @return 角色信息列表（包含create_uname 和 update_uname）
     */
    List<Role> findAll(Map<String,Object> map);

    /**
     * 新建角色信息
     * @param role {deleteFlag = 1,rid nullable}
     */
    @Insert("insert into t_role(rname,description,create_time,create_uid,delete_flag) " +
            "values(#{rname},#{description},now(),#{createUid},1)")
    void save(Role role);


    /**
     * 删除角色
     *
     */
    void delete();



}
