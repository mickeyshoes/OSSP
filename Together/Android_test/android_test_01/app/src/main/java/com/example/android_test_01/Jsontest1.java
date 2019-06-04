package com.example.android_test_01;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Jsontest1 extends AsyncTask<String, Void, String> {

    public ArrayList<User> userlist = new ArrayList<>();

    @Override
    protected void onPreExecute() {

        User user1 = new User();
        user1.ID = "hello";
        user1.name="park";
        user1.age = 15;

        User user2 = new User();
        user2.ID = "hi";
        user2.name="kim";
        user2.age=17;

        userlist.add(user1);
        userlist.add(user2);

    }

    @Override
    protected String doInBackground(String... params) {

        JSONArray jarray = new JSONArray();
        for(int i=0; i< userlist.size(); i++){
            JSONObject sobj = new JSONObject();
            try {
                sobj.put("ID", userlist.get(i).ID);
                sobj.put("name", userlist.get(i).name);
                sobj.put("age", userlist.get(i).age);
                jarray.put(sobj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        try {
            String sendJson = jarray.toString();

            URL url = new URL(params[0]);
            Log.e("출력 url",params[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(sendJson.getBytes("euc-kr"));
            os.flush();
            Log.e("출력 url",params[0]);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            Log.d("input test", jarray.toString());
        }


        return jarray.toString();
    }
}
