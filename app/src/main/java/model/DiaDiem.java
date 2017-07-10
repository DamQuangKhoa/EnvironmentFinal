package model;

import java.io.Serializable;

/**
 * Created by Sky on 24/06/2017.
 */

public class DiaDiem implements Serializable {
    private String tenDuong,khuVuc,thoiGianBatDau,thoiGianKetThuc;
    private long lat,lon;
    private boolean ketXe;
    private int mucDo;
    private int hinhAnh;

    public DiaDiem() {
    }

    public DiaDiem(String tenDuong, String khuVuc, String thoiGianBatDau, long lat, long lon, int mucDo) {
        this.tenDuong = tenDuong;
        this.khuVuc = khuVuc;
        this.thoiGianBatDau = thoiGianBatDau;
        this.lat = lat;
        this.lon = lon;
        this.mucDo = mucDo;
    }

    public String getTenDuong() {
        return tenDuong;
    }
public boolean kiemTraKhongCoGiaTri(String data){
    return tenDuong == null || tenDuong.equalsIgnoreCase("");
}
    public void setTenDuong(String tenDuong) {
        if(kiemTraKhongCoGiaTri(tenDuong)){
            this.tenDuong = "Chưa Có Tên";
        }
        this.tenDuong = tenDuong;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        if(kiemTraKhongCoGiaTri(khuVuc)){
            this.tenDuong = "Chưa Có Khu Vực ";
        }
        this.khuVuc = khuVuc;
    }

    public String getThoiGianBatDau() {

        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(String thoiGianBatDau) {
        if(kiemTraKhongCoGiaTri(thoiGianBatDau)){
            this.thoiGianBatDau = "";
        }
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public String getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(String thoiGianKetThuc) {
        if(kiemTraKhongCoGiaTri(thoiGianKetThuc)){
            this.thoiGianKetThuc = "";
        }
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public boolean isKetXe() {
        return ketXe;
    }

    public void setKetXe(boolean ketXe) {
        this.ketXe = ketXe;
    }

    public int getMucDo() {
        return mucDo;
    }

    public void setMucDo(String mucDo) {
        switch (mucDo){
            case "Nghiêm Trọng":
                this.mucDo = 5;
                break;
            case "Khá Nghiêm Trọng":
                this.mucDo = 4;
                break;
            case "Chưa Tốt":
                this.mucDo = 3;
                break;
            case "Bình Thường":
                this.mucDo = 2;
                break;
            case "Tốt":
                this.mucDo = 1;
                break;
        }
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        if(kiemTraKhongCoGiaTri(hinhAnh+"")){
            this.hinhAnh = 0;
        }
        this.hinhAnh = hinhAnh;
    }
}
