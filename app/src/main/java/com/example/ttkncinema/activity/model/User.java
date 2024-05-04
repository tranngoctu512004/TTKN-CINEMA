package com.example.ttkncinema.activity.model;

public class User {
    String Ho;
    String Ten;
    String Username;
    String Email;
    String Sdt;
    String Matkhau;

    public User(String ho, String ten, String username, String email, String sdt, String matkhau) {
        Ho = ho;
        Ten = ten;
        Username = username;
        Email = email;
        Sdt = sdt;
        Matkhau = matkhau;
    }

    public User() {
    }

    public String getHo() {
        return Ho;
    }

    public void setHo(String ho) {
        Ho = ho;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSdt() {
        return Sdt;
    }

    public void setSdt(String sdt) {
        Sdt = sdt;
    }

    public String getMatkhau() {
        return Matkhau;
    }

    public void setMatkhau(String matkhau) {
        Matkhau = matkhau;
    }
}
