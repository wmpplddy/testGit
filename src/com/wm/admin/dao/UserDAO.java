package com.wm.admin.dao;

import com.wm.admin.domain.User;
import com.wm.admin.dynamic.UserListDynamic;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface UserDAO {


    @Select("select * from t_user where zjm = #{uname} or uname = #{uname} or mail = #{uname} or phone = #{uname}")
    User selectOne(String uname);
    @Select("select * from t_user where zjm = #{zjm}")
    User selectOneByZjm(String zjm);
    @Select("select * from t_user where phone = #{phone}")
    User selectOneByPhone(String phone);
    @Select("select * from t_user where mail = #{mail} ")
    User selectOneByMail(String mail);
    @Select("select * from t_user where uname = #{uname}")
    User selectOneByUname(String uname);

    @Insert("insert into t_user values(" +
            "null,#{uname},md5(#{upass}),#{zjm},#{phone}" +
            ",#{mail},#{truename},#{sex},#{age},now(),#{create_uid},null,null,1,null,null,null,null)")
    void addUser(User user);

    @Update("update t_user set " +
            " truename = #{truename} , mail = #{mail} ,phone = #{phone},sex = #{sex},age = #{age},update_uid = #{update_uid},update_time=now() " +
            " where zjm = #{zjm}")
    void UpdateUser(User user);

    @Update("update t_user set upass = md5(#{upass}) where uid = #{uid}")
    void UpdateUserPwd(User user);

    @Select("select * from t_user where uid = #{uid}")
    User selectByID(Long uid);

    @SelectProvider(value = UserListDynamic.class,method = "dynamicList")
    List<User> selectByPage(Map<String,Object> param);

    @SelectProvider(type = UserListDynamic.class,method = "dynamicListTotalCount")
    int selectTotal(Map<String,Object> param);


    @SelectProvider(type=UserListDynamic.class,method = "selectByPageLike1")
    List<User> selectByPageLike(Map<String,Object> param);

    @SelectProvider(type = UserListDynamic.class,method = "selectByPageLikeTotal12")
    int selectByPageLikeTotal(Map<String,Object> param);

    @Delete("delete from t_user where uid = #{uid}")
    void deleteDB(String uid);

    @Update("update t_user set delete_flag = 2 where uid = #{uid}")
    void updateUserDeleteFlag(String uid);

    @Update("update t_user set delete_flag = #{flag} where uid = #{uid}")
    void changeUserDeleteFlag(Map<String,String> map);

    @Select("select zjm from t_user;")
    List<String> allZJM();


    @Update("update t_user set yl1 = #{path} where zjm = #{u_zjm}")
    void updateUserAvaPath(Map<String,String> map);
}
