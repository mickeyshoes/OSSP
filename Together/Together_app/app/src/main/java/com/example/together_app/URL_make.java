/*
* 개발자 - 임성민
* 개발목적 - url생성을 위함
* */
package com.example.together_app;

public class URL_make {

    public String URL="http://192.168.0.15:8888/dbtest/";
    public String parameter;

    public URL_make(String parameter){
        this.parameter = parameter;
    }

    public String make_url(){
        return URL+parameter;
    }

}
