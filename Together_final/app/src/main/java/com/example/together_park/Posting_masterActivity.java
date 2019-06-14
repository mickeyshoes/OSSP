package com.example.together_park;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Posting_masterActivity extends AppCompatActivity {

    private TextView TextView_Title;
    private TextView TextView_arrival;
    private TextView TextView_departure;
    private TextView TextView_person;
    private TextView TextView_memo;

    TextView TextView_Chat;

    EditText EditText_chat;
    Button Button_send;

    Button Button_final;

    //입력한 댓글 가짐.
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<item> arrayAdapter2;
    item item_value = null;
    String str; //입력한 댓글을 저장하는 객체

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_master);

        ListView ListView_chat;

        ListAdapter adapter = new ListAdapter();

        TextView_Title = findViewById(R.id.TextView_Title);
        TextView_arrival = findViewById(R.id.TextView_arrival);
        TextView_departure = findViewById(R.id.TextView_departure);
        TextView_person = findViewById(R.id.TextView_person);
        TextView_memo = findViewById(R.id.TextView_memo);

        TextView_Chat = (TextView)findViewById(R.id.TextView_Chat);

        ListView_chat = (ListView)this.findViewById(R.id.ListView_chat);
        ListView_chat.setAdapter(adapter);

        EditText_chat = (EditText)this.findViewById(R.id.EditText_chat);
        Button_send = (Button)this.findViewById(R.id.Button_send);

        TextView_memo = findViewById(R.id.TextView_memo);

        Button_final = findViewById(R.id.Button_final);

        //전송버튼 눌렀을 때
        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = EditText_chat.getText().toString();
                if (str.trim().length() != 0) { //댓글에 아무 내용이 없으면 전송되지 않도록.
                    adapter.addItem("id : ", str, "/2019:06:12");
//                    item_value = new item("id", str, "2019:06:12");
//                    list.add(str); //입력값 리스트에 추가하기
//                    ListView_chat.setAdapter(arrayAdapter);
//                    arrayAdapter.notifyDataSetChanged();
                    EditText_chat.setText(""); //입력된값 지우기
                } else {
                    Toast.makeText(Posting_masterActivity.this, "내용이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent(); //작성액티비티에서 제목, 출발지, 목적지, 제한인원 값 읽기.

        String Title = intent.getStringExtra("Title");
        String arrival = intent.getStringExtra("arrival");
        String departure = intent.getStringExtra("departure");
        String person = intent.getStringExtra("person");
        String memo = intent.getStringExtra("memo");

        TextView_departure.setText(departure);
        TextView_Title.setText(Title);
        TextView_arrival.setText(arrival);
        TextView_person.setText(person);
        TextView_memo.setText(memo);

        //게시글에 인원들이 다모였을때 클릭시 콜벤,택시,사다리타기등 버튼이있는 화면으로 전환
        Button_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_final = new Intent(Posting_masterActivity.this, Final_posting.class);
                startActivity(intent_final);
            }
        });
    }
}