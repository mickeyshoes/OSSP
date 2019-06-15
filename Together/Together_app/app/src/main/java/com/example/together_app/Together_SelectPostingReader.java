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

public class Together_SelectPostingReader extends AppCompatActivity {

    //상단 게시글 정보
    private TextView TextView_arrival_join;
    private TextView TextView_departure_join;
    private TextView TextView_person_join;
    private TextView TextView_memo_join;
    private TextView TextView_nowperson_join;
    //댓글
    TextView TextView_Chat;
    EditText EditText_chat_join;
    Button Button_send_join;
    ListView ListView_chat_join;

    //접속한 아이디와 접속한 방 번호
    public String login_ID;
    public String access_post;
    public Together_CommentList_Adapter tca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together__select_posting_reader);

        TextView_departure_join = findViewById(R.id.TextView_departure_join);
        TextView_arrival_join = findViewById(R.id.TextView_arrival_join);
        TextView_person_join = findViewById(R.id.TextView_person_join);
        TextView_memo_join = findViewById(R.id.TextView_memo_join);
        TextView_nowperson_join = (TextView)findViewById(R.id.TextView_nowperson_join);
        TextView_Chat = findViewById(R.id.TextView_Chat);
        EditText_chat_join = findViewById(R.id.EditText_chat_join);
        Button_send_join = findViewById(R.id.Button_send_join);

        tca = new Together_CommentList_Adapter();

        ListView_chat_join = findViewById(R.id.ListView_chat_join);
        ListView_chat_join.setAdapter(tca);

        Intent intent = getIntent();
        login_ID = intent.getStringExtra("login_ID");
        access_post = intent.getStringExtra("access_post");

        existed_comment_list();

        //서버에서 가져온 값을 textview에 설정한다. (게시글 정보)
        String response = "";
        try {
            URL_make url_make = new URL_make("select_post");
            String inputURL = url_make.make_url();
            response = new Selected_Post().execute(inputURL,access_post).get();
            JSONObject jobj = new JSONObject(response);
            TextView_departure_join.setText(jobj.get("PDeparture").toString());
            TextView_arrival_join.setText(jobj.get("PArrival").toString());
            TextView_memo_join.setText(jobj.get("pabout").toString());
            TextView_person_join.setText(jobj.get("plimit").toString());
            inputURL = new URL_make("count_join_member").make_url();
            response = new getJoinNumber().execute(inputURL,access_post).get();
            TextView_nowperson_join.setText(response);
            Log.e("test234", jobj.toString());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //댓글 전송후 저장
        Button_send_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = EditText_chat_join.getText().toString();
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
                            EditText_chat_join.setText("");
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

        Button joingroup = (Button)findViewById(R.id.Button_final_join);
        joingroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL_make url_make = new URL_make("join_group");
                    String inputURL = url_make.make_url();
                    String possibility = new JoinGroup().execute(inputURL, access_post, login_ID).get();
                    Log.e("possibility",possibility);
                    if(possibility.equals(0)){
                        Toast.makeText(getApplicationContext(), "이미 해당그룹에 참여중입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(possibility.equals(1)){
                        Toast.makeText(getApplicationContext(), "참여성공! 댓글을 통하여 장소를 정하세요.", Toast.LENGTH_LONG).show();
                    }
                    else if(possibility.equals(2)){
                        Toast.makeText(getApplicationContext(), "선택하신 게시글의 인원이 가득합니다. :(", Toast.LENGTH_SHORT).show();
                    }
                    else if(possibility.equals(3)){
                        Toast.makeText(getApplicationContext(), "한번에 한개의 게시글에만 참여가능 합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(possibility.equals(4)){
                        Toast.makeText(getApplicationContext(), "게시글 게시시간이 지나서 참여할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    //해당 그룹에 참여한다.
    public class JoinGroup extends AsyncTask<String, Void, String> {

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
                dos.writeBytes("PNum="+params[1]+"&UID="+params[2]);
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
