package com.example.testapp01;

public class UserInformation {

    private String UID;
    private String UPw;
    private String UName;
    private String UTel;

    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUPw() {
        return UPw;
    }
    public void setUPw(String UPw) {
        this.UPw = UPw;
    }

    public String getUName() {
        return UName;
    }
    public void setUName(String UName) {
        this.UName = UName;
    }

    public String getUTel() {
        return UTel;
    }
    public void setUTel(String UTel) {
        this.UTel = UTel;
    }

    public UserInformation(String id, String pw, String name, String tel){
        UID = id;
        UPw = pw;
        UName = name;
        UTel = tel;
    }
}
