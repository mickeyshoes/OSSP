package com.example.together_park;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Posting_join extends AppCompatActivity {

    //상단 게시글 정보
    private TextView TextView_Title_join;
    private TextView TextView_arrival_join;
    private TextView TextView_departure_join;
    private TextView TextView_person_join;
    private TextView TextView_memo_join;

    //댓글
    TextView TextView_Chat;
    EditText EditText_chat_join;
    Button Button_send_join;

    //댓글 ListView
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<item> arrayAdapter2;
    item item_value = null;
    String str; //입력한 댓글을 저장하는 객체

    ListView ListView_chat_join;

    Button Button_game_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_join);

        TextView_Title_join = findViewById(R.id.TextView_Title_join);
        TextView_departure_join = findViewById(R.id.TextView_departure_join);
        TextView_arrival_join = findViewById(R.id.TextView_arrival_join);
        TextView_person_join = findViewById(R.id.TextView_person_join);
        TextView_memo_join = findViewById(R.id.TextView_memo_join);

        TextView_Chat = findViewById(R.id.TextView_Chat);
        EditText_chat_join = findViewById(R.id.EditText_chat_join);
        Button_send_join = findViewById(R.id.Button_send_join);

        ListAdapter adapter = new ListAdapter();

        ListView_chat_join = findViewById(R.id.ListView_chat_join);
        ListView_chat_join.setAdapter(adapter);

        Button_game_join = findViewById(R.id.Button_game_join);

        Intent intent = getIntent();

        String Title = intent.getStringExtra("Title_join");
        String arrival = intent.getStringExtra("arrival_join");
        String departure = intent.getStringExtra("departure_join");
        String person = intent.getStringExtra("person_join");
        String memo = intent.getStringExtra("memo_join");

        TextView_Title_join.setText(Title);
        TextView_departure_join.setText(departure);
        TextView_arrival_join.setText(arrival);
        TextView_person_join.setText(person);
        TextView_memo_join.setText(memo);

        Button_send_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = EditText_chat_join.getText().toString(); //입력값 str에 저장
                if(str.trim().length() != 0){
                    adapter.addItem("id : ", str, "/2019:06:13");
                    EditText_chat_join.setText(""); //지워주기
                } else Toast.makeText(Posting_join.this, "내용이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //네이버 사다리 타기 화면으로 전환
        Button_game_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=%EC%82%AC%EB%8B%A4%EB%A6%AC%ED%83%80%EA%B8%B0"));
                startActivity(webintent2);
            }
        });
    }
}
