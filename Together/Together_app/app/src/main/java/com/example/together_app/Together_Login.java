package com.example.together_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Together_Login extends AppCompatActivity {
    public int print = -1;
    public EditText idtext;
    public EditText pwtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together__login);

        idtext = (EditText) findViewById(R.id.IDtext);
        pwtext = (EditText) findViewById(R.id.Pwtext);
        Button loginButton = (Button) findViewById(R.id.trylogin);
        Button findButton = (Button) findViewById(R.id.findInfo);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                into_login log = new into_login();
                URL_make make_url = new URL_make("app_login");
                String inputURL = make_url.make_url();
                log.execute(inputURL, idtext.getText().toString(), pwtext.getText().toString());

                //값을 불러올때 까지 잠시 딜레이 (0.5초)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(print == 0){
                            Toast.makeText(Together_Login.this,"아이디를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if(print == 1){
                            Intent intent = new Intent(getApplicationContext(), Together_Listview.class);
                            Toast.makeText(Together_Login.this,"로그인 성공!", Toast.LENGTH_SHORT).show();
                            intent.putExtra("login_ID",idtext.getText().toString());
                            idtext.setText("");
                            pwtext.setText("");
                            startActivity(intent);
                        }
                        else if(print ==2){
                            Toast.makeText(Together_Login.this,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Together_Login.this,"값이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },500);

            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Together_FindInfo.class);
                startActivity(intent);
            }
        });

    }

    public class into_login extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            StringBuilder output = new StringBuilder();

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

            return null;
        }
    }

}
