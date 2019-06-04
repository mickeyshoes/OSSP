package com.example.android_test_01;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnectTask extends AsyncTask<Void, Void, String> { // <execute되어 넘겨주는 매개변수 값,,background에서 return하는 값>

    String url = "http://10.0.2.2:8000/";
    String parameter;
    URL url_final;
    HttpURLConnection con;
    InputStreamReader isr;
    BufferedReader reader;

    public void make_url(String parameter){
        this.parameter = parameter;
        if(parameter == null){
            Log.e("about url","파라미터에 아무런 값도 들어오지 않았음.");
        }
        else{
            url = url + parameter;
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder output = new StringBuilder();
        String read = null;
        try {
            while (true) {
                read = reader.readLine().toString();
                if (read == null) {
                    break;
                }
                output.append(read + "/");
            }
            reader.close();
            con.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return output.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            url_final = new URL(url);
            con = (HttpURLConnection)url_final.openConnection();

            if(con != null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                isr = new InputStreamReader(con.getInputStream());
                reader = new BufferedReader(isr);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("test01","주소가 잘못되었음.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test01","연결이 잘못되었음.");
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
