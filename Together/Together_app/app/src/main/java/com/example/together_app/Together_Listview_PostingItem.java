/*
* 개발자 - 박건형
* 개발목적 - 게시글에 대한 정보가 담길 객체 정의*/
package com.example.together_app;

public class Together_Listview_PostingItem {

    private String PNum;
    private String PID;
    private String PAbout;
    private String PValidtime;
    private String PDeparture;
    private String PArrival;
    private String PLimit;

    public Together_Listview_PostingItem(String PDeparture, String PArrival, String PLimit){
        this.PDeparture = PDeparture;
        this.PArrival = PArrival;
        this.PLimit = PLimit;

    }

    public Together_Listview_PostingItem(String PNum, String PID, String PAbout, String PValidtime, String PDeparture, String PArrival, String PLimit){
        this.PNum = PNum;
        this.PID = PID;
        this.PAbout = PAbout;
        this.PValidtime = PValidtime;
        this.PDeparture = PDeparture;
        this.PArrival = PArrival;
        this.PLimit = PLimit;
    }

    public String getPNum() {
        return PNum;
    }

    public void setPNum(String PNum) {
        this.PNum = PNum;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPAbout() {
        return PAbout;
    }

    public void setPAbout(String PAbout) {
        this.PAbout = PAbout;
    }

    public String getPValidtime() {
        return PValidtime;
    }

    public void setPValidtime(String PValidtime) {
        this.PValidtime = PValidtime;
    }

    public String getPDeparture() {
        return PDeparture;
    }

    public void setPDeparture(String PDeparture) {
        this.PDeparture = PDeparture;
    }

    public String getPArrival() {
        return PArrival;
    }

    public void setPArrival(String PArrival) {
        this.PArrival = PArrival;
    }

    public String getPLimit() {
        return PLimit;
    }

    public void setPLimit(String PLimit) {
        this.PLimit = PLimit;
    }
}
