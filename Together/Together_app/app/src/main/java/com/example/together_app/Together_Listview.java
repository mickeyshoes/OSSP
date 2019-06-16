/*
* 개발자 - 임성민, 박건형
* 개발목적 - 게시글 출력 및 그룹참여 해제*/
package com.example.together_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Together_Listview extends AppCompatActivity {
    private ArrayList<Together_Listview_PostingItem> postingItems;
    public String login_ID;
    public Together_Listview_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together__listview);

        Intent intent = getIntent();
        login_ID = intent.getStringExtra("login_ID");
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new Together_Listview_Adapter();
        postingItems = new ArrayList<>();
        listView.setAdapter(adapter);

        Button inputbtn = (Button) findViewById(R.id.inputbtn);
        inputbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Together_Posting.class);
                intent.putExtra("login_ID",login_ID);
                startActivity(intent);
            }
        });

        //참여하고 있는 그룹을 간편하게 삭제
        Button refreshbtn = (Button) findViewById(R.id.refreshbtn);
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    URL_make url_make = new URL_make("leave_group");
                    String inputURL = url_make.make_url();
                    String result = new DeleteJoin().execute(inputURL, login_ID).get();
                    if(result.equals("1")){
                        Toast.makeText(getApplicationContext(), "참여한 게시글의 참여관계가 삭제 완료되었습니다.(글 작성자는 글 삭제)", Toast.LENGTH_SHORT).show();
                    }
                    else if(result.equals("2")){
                        Toast.makeText(getApplicationContext(), "참여하고 있던 게시글이 유효시간을 벗어났습니다. 다른 게시글에 참여하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if(result.equals("3")){
                        Toast.makeText(getApplicationContext(), "참여하고 있는 게시글이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(true){
                        Toast.makeText(getApplicationContext(), "입력된 값의 오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        existed_posting_list();

        //각각의 게시글이 클릭되었을때 일어날 행동 정의
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String access_post = postingItems.get(position).getPNum();
                String name = postingItems.get(position).getPID();
                Log.e("id match", "intent id = "+login_ID+" item id ="+name);
                // 로그인 아이디와 게시글 작성자 아이디가 같으면 작성자 화면의 게시글로 이동
                if(login_ID.equals(name)){
                    Intent intent1 = new Intent(getApplicationContext(), Together_SelectPostingWriter.class);
                    intent1.putExtra("login_ID",login_ID);
                    intent1.putExtra("access_post",access_post);
                    intent1.putExtra("pabout",postingItems.get(position).getPAbout());
                    intent1.putExtra("pdeparture",postingItems.get(position).getPDeparture());
                    intent1.putExtra("parrival", postingItems.get(position).getPArrival());
                    intent1.putExtra("plimit", postingItems.get(position).getPLimit());
                    startActivity(intent1);
                }
                // 로그인 아이디와 게시글 작성자 아이디가 다르면 게시글 화면으로 이동
                else{
                    Intent intent2 = new Intent(getApplicationContext(), Together_SelectPostingReader.class);
                    intent2.putExtra("login_ID",login_ID);
                    intent2.putExtra("access_post",access_post);
                    startActivity(intent2);
                }
            }
        });

    }

    //유효시간이 남아있는 게시글의 리스트를 화면에 출력한다.
    public void existed_posting_list(){

        URL_make make_url = new URL_make("existed_post");
        String inputURL = make_url.make_url();
        String response = "";
        try {
            response = new showValidPost().execute(inputURL).get();
            JSONArray jsonArray = new JSONArray(response);
            Log.e("ary", jsonArray.toString());
            for(int i =0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Together_Listview_PostingItem tlp = new Together_Listview_PostingItem(jsonObject.get("PNum").toString(), jsonObject.get("PID").toString(), jsonObject.get("pabout").toString(), jsonObject.get("PValidtime").toString(), jsonObject.get("PDeparture").toString(), jsonObject.get("PArrival").toString(), jsonObject.get("plimit").toString());
                postingItems.add(tlp);
                adapter.addItem(jsonObject.get("PDeparture").toString(), jsonObject.get("PArrival").toString(), jsonObject.get("plimit").toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // 현재시간에 비교하여 유효시간이 아직 남은 게시글을 출력
    public class showValidPost extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder output = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Accept", "application/x-www-form-urlencoded;charset=UTF-8");

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
    // 그룹에 참여되어 있으나 다른 그룹에 참여하고 싶거나 그룹을 나가고 싶을때 실행
    public class DeleteJoin extends AsyncTask<String, Void, String> {

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
                dos.writeBytes("login_ID="+params[1]);
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
