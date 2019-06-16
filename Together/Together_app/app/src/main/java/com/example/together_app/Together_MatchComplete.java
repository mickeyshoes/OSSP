/*
* 개발자 - 임성민, 박건형
* 개발목적 - 최종 그룹이 맺어진 게시자가 최종적으로 확인 할 수 있는 액티비티
*            콜벤, 택시번호 조회와 사다리타기 기능을 확인 할 수 있다.*/
package com.example.together_app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

//게시글에 인원이 다 모여 완료버튼 클릭 시 넘어오는 액티비티, 콜벤전화, 네이버 사다리타기, 작성화면에 지정한 지도값 가져오기.
public class Together_MatchComplete extends AppCompatActivity {

    LinearLayout Taxi;
    LinearLayout CallBan;
    LinearLayout Ladder_Game;

    public String in_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together__match_complete);

        Taxi = findViewById(R.id.Taxi);
        CallBan = findViewById(R.id.CallBan);
        Ladder_Game = findViewById(R.id.Ladder_Game);
        TextView groupview = (TextView)findViewById(R.id.group_member);

        //그룹에 참여한 사용자의 아이디를 나타내고,
        // 그들이 참여했던 게시글의 유효시간을 현재로 세팅하여 게시글 목록에서 보이지 않게 한다.
        try {
            Intent intent = getIntent();
            String access_post = intent.getStringExtra("access_post");

            URL_make make_url = new URL_make("change_validtime");
            String change_time = make_url.make_url();
            String change_result = new ValidtimeChange().execute(change_time, access_post).get();
            //서버에서 받아온 결과값에 대한 동작
            if(change_result != "0"){

                URL_make url_make = new URL_make("count_join_member");
                String urlInput = url_make.make_url();
                String result = new getJoinNumber().execute(urlInput, access_post).get();
                Log.e("print result", result);
                in_group = "";
                JSONArray jsonArray = new JSONArray(result);
                Log.e("ary", jsonArray.toString());
                for(int i =0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if( i ==0){
                        in_group = jsonObject.get("JID").toString();
                    }
                    else{
                        in_group = in_group +", "+ jsonObject.get("JID");
                    }
                }
                Log.e("print result2",in_group);
            }
            else{
                Toast.makeText(getApplicationContext(),"유효시간 갱신문제! 사용에는 문제 없습니다.",Toast.LENGTH_LONG).show();
            }
            groupview.setText(in_group + "!"+"\nThank you for your day!");

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //택시에 관한 정보
        Taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Taxiintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=%EC%B2%9C%EC%95%88+%EC%BD%9C%ED%83%9D%EC%8B%9C"));
                startActivity(Taxiintent);
            }
        });

        //콜벤 전화버튼 클릭 시 전화화면으로 전환
        CallBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialintent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:041-584-7000"));
                startActivity(dialintent);
            }
        });

        //사다리타기 버튼 클릭 시 네이버 사다리타기 게임 화면으로 전환
        Ladder_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=%EC%82%AC%EB%8B%A4%EB%A6%AC%ED%83%80%EA%B8%B0"));
                startActivity(webintent);
            }
        });
    }
    // 그룹이 맺어진 게시글의 유효시간을 변경하여 다른 사용자가 완료된 게시글에 접근하지 못하게 유효시간을 현재시간으로 변경
    public class ValidtimeChange extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder output = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Accept", "application/x-www-form-urlencoded;charset=UTF-8");

                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes("Pnum="+params[1]);
                Log.e("pnum value",params[1]);
                dos.flush();
                dos.close();

                String results = "";
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

                    while((results = br.readLine()) != null){
                        output.append(results);
                    }
                    br.close();

                }
                else{
                    //404에 대한 오류가 되겟음
                }
                con.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return output.toString();
        }
    }
    // 그룹에 참여된 사람들의 목록을 받아옴.
    public class getJoinNumber extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder output = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Accept", "application/x-www-form-urlencoded;charset=UTF-8");

                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes("PNum="+params[1]+"&SelectNum="+"2");
                dos.flush();
                dos.close();

                String results = "";
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

                    while((results = br.readLine()) != null){
                        output.append(results);
                    }
                    br.close();

                }
                else{
                    //404에 대한 오류가 되겟음
                }
                con.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return output.toString();
        }
    }
}
