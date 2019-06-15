package com.example.together_app;

public class Together_CommentItem {

    private int CNum;
    private String CID;
    private String CTime;
    private String CAbout;

    public Together_CommentItem(String CID, String CAbout, String CTime){
        this.CID = CID;
        this.CAbout = CAbout;
        this.CTime = CTime;

    }

    public Together_CommentItem(int CNum, String CID, String CTime, String CAbout){
        this.CNum = CNum;
        this.CID = CID;
        this.CTime = CTime;
        this.CAbout = CAbout;

    }

    public int getCNum() {
        return CNum;
    }

    public void setCNum(int CNum) {
        this.CNum = CNum;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getCTime() {
        return CTime;
    }

    public void setCTime(String CTime) {
        this.CTime = CTime;
    }

    public String getCAbout() {
        return CAbout;
    }

    public void setCAbout(String CAbout) {
        this.CAbout = CAbout;
    }
}
