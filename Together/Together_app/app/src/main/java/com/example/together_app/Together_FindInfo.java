package com.example.together_app;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Together_FindInfo extends AppCompatActivity {

    public EditText name01;
    public EditText tel01;
    public EditText id01;
    public EditText tel02;
    public String inputURL;
    public String findUserID = "";
    public String findUserPW = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together__find_info);

        name01 = (EditText) findViewById(R.id.Nametext01);
        tel01 = (EditText) findViewById(R.id.Teltext01);
        tel02 = (EditText) findViewById(R.id.Teltext02);
        id01 = (EditText) findViewById(R.id.IDtext01);

        //아이디를 분실하였을때 찾음
        //asynctask가 비동기라서 처리시간이 늦기 때문에 0.5초 sleep
        Button findID = (Button)findViewById(R.id.FindIDBtn);
        findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    URL_make make_url = new URL_make("find_id");
                    inputURL = make_url.make_url();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                findUserID = new Find_Info().execute(inputURL,name01.getText().toString(),tel01.getText().toString(), "1").get();
                                if(findUserID.equals("0")){
                                    Toast.makeText(Together_FindInfo.this,"등록된 정보의 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(Together_FindInfo.this,"등록된 아이디는 "+findUserID+" 입니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    },500);

            }
        });

        Button findPWbtn = (Button)findViewById(R.id.FindPWBtn);
        findPWbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL_make make_url = new URL_make("find_pw");
                inputURL = make_url.make_url();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findUserPW = new Find_Info().execute(inputURL, id01.getText().toString(), tel02.getText().toString(), "2").get();
                            if(findUserPW.equals("0")){
                                Toast.makeText(Together_FindInfo.this,"입력하신 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Together_FindInfo.this,"등록된 비밀번호는 "+findUserPW+" 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                },500);

            }
        });



    }

    public class Find_Info extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder output = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                if(params[3].equals("1")){
                    dos.writeBytes("UserName="+params[1]+"&UserTel="+params[2]);
                }
                else if(params[3].equals("2")){
                    dos.writeBytes("UserID="+params[1]+"&UserTel="+params[2]);
                }

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

                //값 확인
                Log.e("output:",output.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return output.toString();
        }
    }
}
