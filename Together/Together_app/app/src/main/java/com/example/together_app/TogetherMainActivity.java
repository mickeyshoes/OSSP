/*
* 개발자 - 임성민
* 개발목적 - 사용자에게 애플리케이션에 대한 간단한 설명과 회원가입, 로그인을 할 수 있게 안내하는 액티비티
* */
package com.example.together_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class TogetherMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together_main);

        Button makeaccount = (Button)findViewById(R.id.CreateAccountBtn);
        Button signin = (Button)findViewById(R.id.SignInBtn);

        // 회원가입 창으로 이동
        makeaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Together_SignupMember.class);
                startActivity(intent);

            }
        });
        // 로그인창으로 이동
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Together_Login.class);
                startActivity(intent);

            }
        });

    }


}
