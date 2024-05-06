package com.wm.admin.domain;

import java.io.Serializable;
import java.sql.Date;

public class Role implements Serializable {
    private Long rid;
    private String rname;
    private String description;

    private Date createTime;
    private Long createUid;
    private Date updateTime;
    private Long updateUid;
    private Integer deleteFlag;
    private String yl1;
    private String yl2;
    /**πÿ¡™ Ù–‘ */
    private String create_uname;
    private String update_uname;

    public String getCreate_uname() {
        return create_uname;
    }
    public void setCreate_uname(String create_uname) {
        this.create_uname = create_uname;
    }
    public String getUpdate_uname() {
        return update_uname;
    }
    public void setUpdate_uname(String update_uname) {
        this.update_uname = update_uname;
    }
    public Role() {
    }

    public String toString() {
        return "Role{" +
                "rid=" + rid +
                ", rname='" + rname + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", createUid=" + createUid +
                ", updateTime=" + updateTime +
                ", updateUid=" + updateUid +
                ", deleteFlag=" + deleteFlag +
                ", yl1='" + yl1 + '\'' +
                ", yl2='" + yl2 + '\'' +
                '}';
    }

    public Role(Long rid, String rname, String description, Date createTime, Long createUid, Date updateTime, Long updateUid, Integer deleteFlag, String yl1, String yl2) {
        this.rid = rid;
        this.rname = rname;
        this.description = description;
        this.createTime = createTime;
        this.createUid = createUid;
        this.updateTime = updateTime;
        this.updateUid = updateUid;
        this.deleteFlag = deleteFlag;
        this.yl1 = yl1;
        this.yl2 = yl2;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getYl1() {
        return yl1;
    }

    public void setYl1(String yl1) {
        this.yl1 = yl1;
    }

    public String getYl2() {
        return yl2;
    }

    public void setYl2(String yl2) {
        this.yl2 = yl2;
    }
}
