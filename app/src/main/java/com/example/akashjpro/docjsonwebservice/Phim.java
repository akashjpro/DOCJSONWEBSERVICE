package com.example.akashjpro.docjsonwebservice;

/**
 * Created by Akashjpro on 10/6/2016.
 */

public class Phim {
    private Integer id ;
    private String tenPhim;
    private String theLoai;
    private String quocGia;

    public Phim() {
    }

    public Phim(Integer id, String tenPhim, String theLoai, String quocGia) {
        this.id = id;
        this.tenPhim = tenPhim;
        this.theLoai = theLoai;
        this.quocGia = quocGia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getQuocGia() {
        return quocGia;
    }

    public void setQuocGia(String quocGia) {
        this.quocGia = quocGia;
    }
}
