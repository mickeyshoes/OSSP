package com.example.testapp01;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView txtmsg01;
    public static String seturl = "http://localhost:8000/print/print_hello";
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtmsg01 = (TextView) findViewById(R.id.txtmsg01);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ConnectThread ct = new ConnectThread(seturl);
                ct.start();
            }

        });

    }

    class ConnectThread extends Thread{
        String seturl;

        public ConnectThread(String in){
            seturl = in;
        }

        public void run(){

            try {
                final String output = request(seturl);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        txtmsg01.setText(output);
                    }
                });

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private String request(String seturl) {
            StringBuilder output = new StringBuilder(); // 가변형 String 문자열의 변경이 많은 경우 사용하면 좋음 싱글스레드 환경에서 속도가 buffer보다 빠름
            try{
                URL url = new URL(seturl);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                if(con != null){
                    con.setConnectTimeout(10000); // 연결대기시간 설정
                    con.setRequestMethod("GET");
                    // 입출력이 가능하게 해줌
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    int reqcode = con.getResponseCode();
                    Log.d("인터넷 연결은 되신겁니까?", String.valueOf(reqcode));

                    InputStreamReader isr = new InputStreamReader(con.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    String line = null;

                    while(true){
                        line = reader.readLine();
                        if(line == null){
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                    con.disconnect();
                }

            } catch(Exception e){
                Log.e("SampleHTTP", "Exception in processing response.", e);
                e.printStackTrace();
            }
            return output.toString();
        }

    }

}
