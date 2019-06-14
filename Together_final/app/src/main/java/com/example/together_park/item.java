package com.example.together_park;

public class item {
    public String id;
    public String chat;
    public String datatime;

    public item(String _id, String _chat, String _datatime){
        this.id = _id;
        this.chat = _chat;
        this.datatime = _datatime;
    }

    public String getId(){
        return id;
    }
    public String getChat(){
        return chat;
    }
    public String getDatatime(){
        return  datatime;
    }
}
