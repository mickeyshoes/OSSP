package com.example.android_test_01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login_test_act extends AppCompatActivity {

    private String server_url = "http://192.168.0.15:8888/dbtest/app_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test_act);

        final EditText idtext = (EditText) findViewById(R.id.editText);
        final EditText pwtext = (EditText) findViewById(R.id.editText2);

        Button loginbutton = (Button) findViewById(R.id.trylogin);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("id:",idtext.getText().toString());
                Log.e("PW", pwtext.getText().toString());

                into_login into_log = new into_login();
                into_log.execute(server_url,idtext.getText().toString(), pwtext.getText().toString());

            }
        });
    }



    class into_login extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            StringBuilder output = new StringBuilder();
            int print = 0;

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes("UserID="+params[1]+"&UserPW="+params[2]);
                dos.flush();
                dos.close();

                InputStreamReader is = new InputStreamReader(con.getInputStream());
                BufferedReader reader = new BufferedReader(is);
                String results = "";

                while(true){
                    results = reader.readLine();
                    if(results == null){
                        break;
                    }output.append(results);
                }
                Log.e("output:",output.toString());

                results = output.toString();
                print = Integer.parseInt(results);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return print;
        }
    }
}
