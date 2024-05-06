package com.wm.admin.dao;

import com.wm.admin.domain.Doctor;
import com.wm.admin.domain.Knit;
import com.wm.admin.dynamic.DoctorDY;
import org.apache.ibatis.annotations.*;

import javax.print.Doc;
import java.util.List;
import java.util.Map;

public interface DocDAO {

    //所有科室
    @Select("select * from knit")
    List<Knit> findAllKnit();

    @Select("select * from knit where k_id = #{kid}")
    Knit findOneKnit(String kid);


    //所有医生
    @Results(
            {
                    @Result(id = true,property = "d_id",column="d_id"),
                    @Result(property = "dname",column = "dname"),
                    @Result(property = "cardid",column = "cardid"),
                    @Result(property = "age",column = "age"),
                    @Result(property = "sex",column = "sex"),
                    @Result(property = "description",column = "description"),
                    @Result(property = "level",column = "level"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "d_kid",column = "d_kid"),
                    @Result(property = "delete_flag",column = "delete_flag"),
                    @Result(property = "dl1",column = "dl1"),
                    @Result(property = "dl2",column = "dl2"),
                    @Result(property = "knit",column = "d_kid",javaType = Knit.class,one = @One(select = "findOneKnit")),
            }
    )
    @SelectProvider(type = DoctorDY.class,method = "listPaged")
    List<Doctor> findAllDoc(Map<String,Object> param);

    @SelectProvider(type = DoctorDY.class,method = "listPagedCount")
    int findAllDocCount(Map<String,Object> param);

    //按照科室展示医生
    @Select("select * from doctor where d_kid = (select k_id from knit where kname = #{kname})")
    List<Doctor> findDocByKnit(String kname);



}
