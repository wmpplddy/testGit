package com.wm.admin.domain;

import java.time.LocalDateTime;

public class Doctor {
    private Integer d_id;
    private String dname; //
    private String cardid; //身份证
    private String tel ;//电话
    private Integer age;
    private String sex;
    private String description; //描述
    private String level; //等级 比如主任医师
    private LocalDateTime create_time;
    private Integer d_kid; //科室id
    private Integer delete_flag;//删除标识 1未删除
    private String dl1;//预留字段
    private String dl2;//预留字段

    /**关联属性*/
    private Knit knit;


    public Knit getKnit() {
        return knit;
    }

    public void setKnit(Knit knit) {
        this.knit = knit;
    }

    public Integer getD_id() {
        return d_id;
    }

    public void setD_id(Integer d_id) {
        this.d_id = d_id;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    public Integer getD_kid() {
        return d_kid;
    }

    public void setD_kid(Integer d_kid) {
        this.d_kid = d_kid;
    }

    public Integer getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(Integer delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getDl1() {
        return dl1;
    }

    public void setDl1(String dl1) {
        this.dl1 = dl1;
    }

    public String getDl2() {
        return dl2;
    }

    public void setDl2(String dl2) {
        this.dl2 = dl2;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "d_id=" + d_id +
                ", dname='" + dname + '\'' +
                ", cardid='" + cardid + '\'' +
                ", tel='" + tel + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                ", create_time=" + create_time +
                ", d_kid=" + d_kid +
                ", delete_flag=" + delete_flag +
                ", dl1='" + dl1 + '\'' +
                ", dl2='" + dl2 + '\'' +
                ", knit=" + knit +
                '}';
    }
}
