package model;

/**
 * Created by Sky on 12/08/2017.
 */

public class DiaDiemChinh {
    private String duong,khuvuc,mucdo,loai,thoiGianBD,thoiGianKT,hinhAnh,tinhThanh,phuong;
    private double latitude,longtitude;

    @Override
    public String toString() {
        return "DiaDiemChinh{" +
                "duong='" + duong + '\'' +
                ", khuvuc='" + khuvuc + '\'' +
                ", mucdo='" + mucdo + '\'' +
                ", loai='" + loai + '\'' +
                ", thoiGianBD='" + thoiGianBD + '\'' +
                ", thoiGianKT='" + thoiGianKT + '\'' +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", tinhThanh='" + tinhThanh + '\'' +
                ", phuong='" + phuong + '\'' +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                '}';
    }

    public String getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(String tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    public String getPhuong() {
        return phuong;
    }

    public void setPhuong(String phuong) {
        this.phuong = phuong;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public DiaDiemChinh(String duong, String khuvuc, String mucdo, String loai, String thoiGianBD, double latitude, double longtitude, String hinhanh,String tinh,String phuong) {
        this.duong = duong;
        this.khuvuc = khuvuc;
        this.mucdo = mucdo;
        this.loai = loai;
        this.thoiGianBD = thoiGianBD;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.hinhAnh= hinhanh;
        this.tinhThanh = tinh;
        this.phuong = phuong;
    }

    public String getDuong() {
        return duong;
    }

    public void setDuong(String duong) {
        this.duong = duong;
    }

    public String getKhuvuc() {
        return khuvuc;
    }

    public void setKhuvuc(String khuvuc) {
        this.khuvuc = khuvuc;
    }

    public String getMucdo() {
        return mucdo;
    }

    public void setMucdo(String mucdo) {
        this.mucdo = mucdo;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getThoiGianBD() {
        return thoiGianBD;
    }

    public void setThoiGianBD(String thoiGianBD) {
        this.thoiGianBD = thoiGianBD;
    }

    public String getThoiGianKT() {
        return thoiGianKT;
    }

    public void setThoiGianKT(String thoiGianKT) {
        this.thoiGianKT = thoiGianKT;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
