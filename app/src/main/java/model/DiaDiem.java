package model;

import java.io.Serializable;

/**
 * Created by Sky on 24/06/2017.
 */

public class DiaDiem implements Serializable {
    private String tenDuong,khuVuc,thoiGianBatDau,thoiGianKetThuc,mucDo;
    private double lat,lon;
    private boolean ketXe;
    private int mucDoInt;
    private int hinhAnh;
    private String loai;
    private int khuVucInt;

    public DiaDiem() {
    }
    public DiaDiem(String tenDuong,String thoiGianBatDau,String loai,int mucdo, int khuvuc,int hinhAnh,double lat,double lon){
        this.tenDuong = tenDuong;
        this.thoiGianBatDau= thoiGianBatDau;
        this.loai = loai;
        this.mucDo = changeMDIntToString(mucdo);
        this.mucDoInt = mucdo;
        this.khuVuc = changeKVIntToString(khuvuc);
        this.khuVucInt=khuvuc;
        this.lat = lat;
        this.lon = lon;
    }
    public DiaDiem(String tenDuong, String khuVuc, String thoiGianBatDau, long lat, long lon, int mucDoInt) {
        this.tenDuong = tenDuong;
        this.khuVuc = khuVuc;
        this.thoiGianBatDau = thoiGianBatDau;
        this.lat = lat;
        this.lon = lon;
        this.mucDoInt = mucDoInt;
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


    public String getKhuVuc()
    {

        return khuVuc;
    }
    public int changeMDStringToInt(String mucdo){
        switch (mucdo){
            case "Nghiêm Trọng":
                return 5;
            case "Khá Nghiêm Trọng":
                return 4;
            case "Chưa Tốt":
                return 3;
            case "Bình Thường":
                return 2;
            case "Tốt":
                return 1;
            default:
                return 1;
        }
    }

    @Override
    public String toString() {
        return "DiaDiem{" +
                "tenDuong='" + tenDuong + '\'' +
                ", khuVuc='" + khuVuc + '\'' +
                ", thoiGianBatDau='" + thoiGianBatDau + '\'' +
                ", thoiGianKetThuc='" + thoiGianKetThuc + '\'' +
                ", mucDo='" + mucDo + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", ketXe=" + ketXe +
                ", hinhAnh=" + hinhAnh +
                ", loai='" + loai + '\'' +
                '}';
    }

    public String changeMDIntToString(int mucdo){
        switch (mucdo){
            case 5:
                return "Nghiêm Trọng";
            case 4:
                return "Khá Nghiêm Trọng";
            case 3:
                return "Chưa Tốt";
            case 2:
                return "Bình Thường";
            case 1:
                return "Tốt";
            default:
                return "Tốt";
        }
    }


    public int changeKVStringToInt(String khuvuc){
        switch (khuvuc){
            case "Quận 1" :
                return 1;
            case "Quận 2" :
                return 2;
            case "Quận 3" :
                return 3;
            case "Quận 4" :
                return 4;
            case "Quận 5" :
                return 5;
            case "Quận 6" :
                return 6;
            case "Quận 7" :
                return 7;
            case "Quận 8" :
                return 8;
            case "Quận 9" :
                return 9;
            case "Quận Go Vap" :
                return 10;
            case "Quận Binh Thanh" :
                return 11;
            case "Quận Thu Duc" :
                return 12;
            case "Quận Binh Chanh" :
                return 13;
            case "Quận Cu Chi" :
                return 14;
            case "Quận Hoc Mon" :
                return 15;
            case "Quận Ben Thanh" :
                return 16;
            default:
                return 16;
        }
    }
    public String changeKVIntToString(int khuvuc){
        switch (khuvuc){
            case 1:
                return "Quận 1";
            case 2:
                return "Quận 2";
            case 3:
                return "Quận 3";
            case 4:
                return "Quận 4";
            case 5:
                return "Quận 5";
            case 6:
                return "Quận 6";
            case 7:
                return "Quận 7";
            case 8:
                return "Quận 8";
            case 9:
                return "Quận 9";
            case 10:
                return "Quận Go Vap";
            case 11:
                return "Quận Binh Thanh";
            case 12:
                return "Quận Thu Duc";
            case 13:
                return "Quận Binh Chanh";
            case 14:
                return "Quận Cu Chi";
            case 15:
                return "Quận Hoc Mon";
            case 16:
                return "Quận Ben Thanh";
            default:
                return "Quận Ben Thanh";
        }
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isKetXe() {
        return ketXe;
    }

    public void setKetXe(boolean ketXe) {
        this.ketXe = ketXe;
    }

    public int getMucDoInt() {
        return mucDoInt;
    }

    public void setMucDoInt(String mucDoInt) {
        switch (mucDoInt){
            case "Nghiêm Trọng":
                this.mucDoInt = 5;
                break;
            case "Khá Nghiêm Trọng":
                this.mucDoInt = 4;
                break;
            case "Chưa Tốt":
                this.mucDoInt = 3;
                break;
            case "Bình Thường":
                this.mucDoInt = 2;
                break;
            case "Tốt":
                this.mucDoInt = 1;
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
