/*
*개발자 - 박건형, 임성민
 *개발목적 - 게시글 목록 중 클릭한 게시글이 자신이 작성한 사람이 보게 될 액티비티
             그룹완료의 기능이 있다.*/
package com.example.together_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Together_SelectPostingWriter extends AppCompatActivity {

    private TextView TextView_arrival;
    private TextView TextView_departure;
    private TextView TextView_person;
    private TextView TextView_memo;
    private TextView TextView_nowperson;

    public String login_ID;
    public String access_post;
    public Together_CommentList_Adapter tca;

    TextView TextView_Chat;
    EditText EditText_chat;
    Button Button_send;
    Button Button_final;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together__select_posting_writer);

        Intent intent = getIntent();
        login_ID = intent.getStringExtra("login_ID");
        access_post = intent.getStringExtra("access_post");

        ListView ListView_chat;
        tca = new Together_CommentList_Adapter();

        TextView_arrival = findViewById(R.id.TextView_arrival);
        TextView_departure = findViewById(R.id.TextView_departure);
        TextView_person = findViewById(R.id.TextView_person);
        TextView_memo = findViewById(R.id.TextView_memo);

        TextView_Chat = (TextView)findViewById(R.id.TextView_Chat);
        TextView_nowperson = (TextView)findViewById(R.id.TextView_nowperson);
        ListView_chat = (ListView)this.findViewById(R.id.ListView_chat);
        ListView_chat.setAdapter(tca);

        EditText_chat = (EditText)this.findViewById(R.id.EditText_chat);
        Button_send = (Button)this.findViewById(R.id.Button_send);

        TextView_memo = findViewById(R.id.TextView_memo);
        Button_final = findViewById(R.id.Button_final);

        existed_comment_list();

        //서버에서 가져온 값을 textview에 설정한다. (게시글 정보)
        String response = "";
        try {
            URL_make url_make = new URL_make("select_post");
            String inputURL = url_make.make_url();
            response = new Selected_Post().execute(inputURL,access_post).get();
            JSONObject jobj = new JSONObject(response);
            TextView_departure.setText(jobj.get("PDeparture").toString());
            TextView_arrival.setText(jobj.get("PArrival").toString());
            TextView_memo.setText(jobj.get("pabout").toString());
            TextView_person.setText(jobj.get("plimit").toString());
            inputURL = new URL_make("count_join_member").make_url();
            response = new getJoinNumber().execute(inputURL,access_post).get();
            TextView_nowperson.setText(response);
            Log.e("test234", jobj.toString());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(TextView_person.getText().toString().equals("제한인원")){
            Toast.makeText(getApplicationContext(), "유효시간이 지나간 게시글에 접근하였습니다. 다른 게시글에 접근하세요.", Toast.LENGTH_LONG).show();
        }

        //댓글 작성 버튼을 눌렀을때 서버에 작성한 댓글을 넣는다.
        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = EditText_chat.getText().toString();
                if(comment.trim().length() != 0){
                    URL_make url_make = new URL_make("write_comment");
                    String inputURL = url_make.make_url();
                    try {
                        String results = new WriteComment().execute(inputURL, access_post,login_ID, comment).get();
                        if(results.equals("0")){
                            Toast.makeText(getApplicationContext(), "댓글을 다시 작성하여 주십시오. (올바르지 않은 입력값)", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "작성 완료", Toast.LENGTH_SHORT).show();
                            //tca.addItem(login_ID,comment,results);
                            tca.commentItems.clear();
                            existed_comment_list();
                            EditText_chat.setText("");
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 그룹이 완료되었다고 생각되면 버튼을 클릭하여 최종 액티비티로 넘어간다.
        Button_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), Together_MatchComplete.class);
                intent3.putExtra("access_post", access_post);
                startActivity(intent3);
                finish();
            }
        });

    }

    //현재 존재하는 댓글 목록
    public void existed_comment_list(){

        String response = "";
        try {
            URL_make url_make = new URL_make("existed_comment");
            String inputURL = url_make.make_url();
            response = new showValidComment().execute(inputURL,access_post).get();
            JSONArray jsonArray = new JSONArray(response);
            Log.e("ary", jsonArray.toString());
            for(int i =0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tca.addItem(jsonObject.get("CID").toString(), jsonObject.get("CAbout").toString(), jsonObject.get("CTime").toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 선택된 게시글의 정보를 가져온다.
    public class Selected_Post extends AsyncTask<String, Void, String> {

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
                dos.writeBytes("SelectPost="+access_post);
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

    // 작성한 댓글을 서버에 저장
    public class WriteComment extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder output = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Accept", "application/text; charset=UTF-8");
                /* 한글과 영어의 혼용을 읽지 못함(Dataoutputstream 클래스 자체의 한계
                if(2==1) {
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    dos.writeBytes("UserID=" + params[1] + "&");
                    dos.writeUTF("UserWrite=" + params[2] + "&UserDeparture=" + params[3] + "&UserArrival=" + params[4] + "&UserLimit=" + params[5]);
                    Log.e("inputcheck", "UserID=" + params[1] + "&UserWrite=" + params[2] + "&UserDeparture=" + params[3] + "&UserArrival=" + params[4] + "&UserLimit=" + params[5]);
                    dos.flush();
                    dos.close();
                }
                */
                OutputStream os = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write("postNum=" + params[1] +"&commentID=" + params[2] + "&commentAbout=" + params[3]);
                Log.e("check datas", "postNum=" + params[1] +"&commentID=" + params[2] + "&commentAbout=" + params[3]);
                bw.flush();
                bw.close();

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

    //해당 게시글에 작성한 댓글을 가져옴
    public class showValidComment extends AsyncTask<String, Void, String> {

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
                dos.writeBytes("commentNum="+access_post);
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
    //현재 게시글에 참여한 인원수를 서버에서 찾아온다.
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
                dos.writeBytes("PNum="+params[1]+"&SelectNum="+1);
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
