package com.wm.admin.domain;

public class Knit {
    private Integer k_id;
    private String kname;
    private String loc;
    private String kl1;
    private String kl2;

    public Knit() {
    }

    @Override
    public String toString() {
        return "Knit{" +
                "k_id=" + k_id +
                ", kname='" + kname + '\'' +
                ", loc='" + loc + '\'' +
                ", kl1='" + kl1 + '\'' +
                ", kl2='" + kl2 + '\'' +
                '}';
    }

    public Knit(Integer k_id, String kname, String loc, String kl1, String kl2) {
        this.k_id = k_id;
        this.kname = kname;
        this.loc = loc;
        this.kl1 = kl1;
        this.kl2 = kl2;
    }

    public Integer getK_id() {
        return k_id;
    }

    public void setK_id(Integer k_id) {
        this.k_id = k_id;
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getKl1() {
        return kl1;
    }

    public void setKl1(String kl1) {
        this.kl1 = kl1;
    }

    public String getKl2() {
        return kl2;
    }

    public void setKl2(String kl2) {
        this.kl2 = kl2;
    }
}
