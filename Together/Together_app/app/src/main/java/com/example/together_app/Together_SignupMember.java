package com.example.together_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Together_SignupMember extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together__signup_member);

        TextView nameInput = (TextView)findViewById(R.id.inputname);
        TextView idInput = (TextView)findViewById(R.id.inputid);
        TextView pwInput = (TextView)findViewById(R.id.inputpw01);
        TextView pw2Input = (TextView)findViewById(R.id.inputpw02);
        TextView telInput = (TextView)findViewById(R.id.inputtel);
        Button inputbtn = (Button)findViewById(R.id.signupbtn);
        inputbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String id = idInput.getText().toString();
                String pw = pwInput.getText().toString();
                String pw2 = pw2Input.getText().toString();
                String tel = telInput.getText().toString();
                String idcheck;
                String telcheck;
                String inputURL;
                String inputURL2;

                try {
                    //아이디 중복 체크 url 생성
                    URL_make url_make = new URL_make("check_id");
                    inputURL = url_make.make_url();
                    idcheck = new duplicateIDCheck().execute(inputURL, id).get();

                    //전화번호 중복 체크 url 생성
                    URL_make url_make1 = new URL_make("check_tel");
                    inputURL2 = url_make1.make_url();
                    telcheck = new duplicateTelCheck().execute(inputURL2, tel).get();


                    if(name.length()==0 || id.length()==0 || pw.length()==0 || pw2.length()==0 || tel.length()==0){
                        Toast.makeText(getApplicationContext(), "값을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else if(idcheck.equals("0")){
                        Toast.makeText(getApplicationContext(), "입력한 아이디가 이미 존재합니다.",Toast.LENGTH_SHORT).show();
                        idInput.setText("");
                    }
                    else if(!(pw.equals(pw2))){
                        Toast.makeText(getApplicationContext(), "비밀번호가 서로 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                        pwInput.setText(""); pw2Input.setText("");
                    }
                    else if(telcheck.equals("0")){
                        Toast.makeText(getApplicationContext(), "입력한 전화번호가 이미 존재합니다.",Toast.LENGTH_SHORT).show();
                        telInput.setText("");
                    }
                    else{
                        URL_make url_make2 = new URL_make("add_user");
                        String urls = url_make2.make_url();
                        String result = new SignUpMember().execute(urls,id,pw,name,tel).get();
                        if(result.equals("1")){
                            Toast.makeText(getApplicationContext(), "Welcome Together !",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "입력값이 유효하지 않습니다.",Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    // 아이디 중복 확인
    public class duplicateIDCheck extends AsyncTask<String, Void, String> {

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
                dos.writeBytes("UserID="+params[1]);
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


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return output.toString();
        }
    }

    // 회원정보 데이터베이스 등록
    public class SignUpMember extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder output = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write("UserID="+params[1]+"&UserPW="+params[2]+"&UserName="+params[3]+"&UserTel="+params[4]);
                bw.flush();
                bw.close();

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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return output.toString();
        }
    }

    // 중복된 전화번호 조회
    public class duplicateTelCheck extends AsyncTask<String, Void, String> {

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
                dos.writeBytes("UserTel="+params[1]);
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


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return output.toString();
        }
    }

}
