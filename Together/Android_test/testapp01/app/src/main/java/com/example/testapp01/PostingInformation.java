package com.example.testapp01;
// 객체 모델링
public class PostingInformation {

    //게시글 작성과 관련된 필드
    private String PID;
    private String PAbout;
    private String PPosttime;
    private String PValidtime;
    private String PDeparture;
    private String PArrival;
    private int PLimit;

    //댓글 작성과 관련된 필드
    private int CNum;
    private String CID;
    private String CTime;
    private String CAbout;

    // 게시글 필드 getter / setter
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

    public String getPPosttime() {
        return PPosttime;
    }
    public void setPPosttime(String PPosttime) {
        this.PPosttime = PPosttime;
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

    public int getPLimit() {
        return PLimit;
    }
    public void setPLimit(int PLimit) {
        this.PLimit = PLimit;
    }

    // 댓글 필드 getter / setter
    public int getCNum() {
        return CNum;
    }
    public void setCNum(int CNum) { this.CNum = CNum; }

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

    public PostingInformation(int posting_num, String comment_id, String comment_time, String comment_about){
        CNum = posting_num;
        CID = comment_id;
        CTime = comment_time;
        CAbout = comment_about;

    }

    public PostingInformation(String posting_id, String posting_about, String posting_posttime, String posting_validtime, String departure, String arrival, int limit){

        PID = posting_id;
        PAbout = posting_about;
        PPosttime = posting_posttime;
        PValidtime = posting_validtime;
        PDeparture = departure;
        PArrival = arrival;
        PLimit = limit;
    }
}
