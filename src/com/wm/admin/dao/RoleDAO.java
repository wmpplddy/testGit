package com.wm.admin.dao;

import com.wm.admin.domain.Role;
import org.apache.ibatis.annotations.Insert;

import java.util.List;
import java.util.Map;

public interface RoleDAO {

    /**
     * ���˷�ҳ��ѯǰ
     * ��ù��˷�ҳ�ļ�¼���� �������������в�ѯ
     * �贫�ݹ���������rname (String,null able)
     * @Param rname
     */
    Long total(String rname);


    /**
     * ���˷�ҳ��ѯ��ʾ
     * ����+��ҳ���� -->Map
     * ����������rname(String,null able)
     * ��ҳ������start(int,not null),length(int,not null)
     * @param map {start,length,rname}
     * @return ��ɫ��Ϣ�б�����create_uname �� update_uname��
     */
    List<Role> findAll(Map<String,Object> map);

    /**
     * �½���ɫ��Ϣ
     * @param role {deleteFlag = 1,rid nullable}
     */
    @Insert("insert into t_role(rname,description,create_time,create_uid,delete_flag) " +
            "values(#{rname},#{description},now(),#{createUid},1)")
    void save(Role role);


    /**
     * ɾ����ɫ
     *
     */
    void delete();



}
