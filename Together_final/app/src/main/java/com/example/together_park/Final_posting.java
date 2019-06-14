package com.example.together_park;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.naver.maps.map.NaverMap;

//게시글에 인원이 다 모여 완료버튼 클릭 시 넘어오는 액티비티, 콜벤전화, 네이버 사다리타기, 작성화면에 지정한 지도값 가져오기.
public class Final_posting extends AppCompatActivity {

    Button Button_call;
    Button Button_naver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_posting);

        Button_call = findViewById(R.id.Button_call);
        Button_naver = findViewById(R.id.Button_naver);

        //콜벤 전화버튼 클릭 시 전화화면으로 전환
        Button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialintent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-5573-8468"));
                startActivity(dialintent);
            }
        });

        //사다리타기 버튼 클릭 시 네이버 사다리타기 게임 화면으로 전환
        Button_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=%EC%82%AC%EB%8B%A4%EB%A6%AC%ED%83%80%EA%B8%B0"));
                startActivity(webintent);
            }
        });
    }
}
