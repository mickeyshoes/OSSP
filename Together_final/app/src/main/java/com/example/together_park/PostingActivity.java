package com.example.together_park;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.MarkerIcons;
import com.naver.maps.map.widget.LocationButtonView;

public class PostingActivity extends Activity implements OnMapReadyCallback {

    public MapView mapView;
    public NaverMap naverMap;

    int red = 65536;
    int green = 16711936;
    int yellow = 256;

    //지도에 표시된 마커.
    private Marker marker = new Marker();//학교
    private Marker marker1 = new Marker();//천안역 마크
    private Marker marker2 = new Marker();//아산역
    private Marker marker3 = new Marker();//트라팰리스 탕정면사무소
    private Marker marker4 = new Marker();//천안터미널
    LocationButtonView locationButtonView;

    //private Marker marker_d = new Marker();
    //private Marker marker_a = new Marker();

    //출발지 라디오그룹 및 버튼
    private RadioGroup RadioGroup_departure;
    private RadioButton RadioButton_departuresunmoon;
    private RadioButton RadioButton_departureasan;
    private RadioButton RadioButton_departurecheonan;
    private RadioButton RadioButton_departureterminal;
    private RadioButton RadioButton_departuretra;

    //목적지 라디오 그룹 및 버튼
    private RadioGroup RadioGroup_arrival;
    private RadioButton RadioButton_arrivalsunmoon;
    private RadioButton RadioButton_arrivalasan;
    private RadioButton RadioButton_arrivalcheonan;
    private RadioButton RadioButton_arrivalterminal;
    private RadioButton RadioButton_arrivaltra;

    //제한인원 라디오그룹 및 버튼.
    private RadioGroup RadioGroup_person;
    private RadioButton RadioButton_one;
    private RadioButton RadioButton_two;
    private RadioButton RadioButton_three;

    //메모
    EditText EditText_memo;
   // Memofile memofile = new Memofile(this);

    //사용자가 입력한 제목, 출발지, 목적지, 제한인원 객체
    private TextInputEditText TextInputEditText; //제목
    private String Title; //제목 입력하면 여기에
    private String departure; // 출발지 필드값
    private String arrival; //도착지 필드값
    String person; //제한인원 필드값
    String person_join;
    String memo;

    @SuppressLint("WrongViewCast")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        RadioGroup_departure = findViewById(R.id.RadioGroup_departure);
        RadioButton_departuresunmoon = findViewById(R.id.RadioButton_departuresunmoon);
        RadioButton_departureasan = findViewById(R.id.RadioButton_departuretasan);
        RadioButton_departurecheonan = findViewById(R.id.RadioButton_departurecheonan);
        RadioButton_departureterminal = findViewById(R.id.RadioButton_departureterminal);
        RadioButton_departuretra = findViewById(R.id.RadioButton_departuretra);

        RadioGroup_arrival = findViewById(R.id.RadioGroup_arrival);
        RadioButton_arrivalsunmoon = findViewById(R.id.RadioButton_arrivalsunmoon);
        RadioButton_arrivalasan = findViewById(R.id.RadioButton_arrivalasan);
        RadioButton_arrivalcheonan = findViewById(R.id.RadioButton_arrivalcheonan);
        RadioButton_arrivalterminal = findViewById(R.id.RadioButton_arrivalterminal);
        RadioButton_arrivaltra = findViewById(R.id.RadioButton_arrivaltra);

        RadioGroup_person = findViewById(R.id.RadioGroup_person);
        RadioButton_one = findViewById(R.id.RadioButton_one);
        RadioButton_two = findViewById(R.id.RadioButton_two);
        RadioButton_three = findViewById(R.id.RadioButton_three);

        RadioGroup_departure.setOnCheckedChangeListener(m);
        RadioGroup_arrival.setOnCheckedChangeListener(m);
        RadioGroup_person.setOnCheckedChangeListener(m);

        TextInputEditText = findViewById(R.id.TextInputEditText); //제목 아이디

        EditText_memo = findViewById(R.id.EditText_memo); //메모 아이디

        //네이버 지도 관련
        mapView = findViewById(R.id.map_view);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        Button Button_save = findViewById(R.id.Button_save);

        Button Button_join = findViewById(R.id.Button_join);

        //저장버튼 눌렀을때 제목, 도착지 목적지, 제한인원버튼값을 넘겨줘야함.
        Button_save.setOnClickListener(new View.OnClickListener() { //저장 버튼 눌렀을 때 할 동작
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostingActivity.this, Posting_masterActivity.class);


                Title = TextInputEditText.getText().toString(); //버튼을 눌렀을때 데이터가 들어간채로 넘어갈수잇게
                memo = EditText_memo.getText().toString(); //메모텍스트 저장

                //작성자 권한의 게시글로 넘기는 동작.
                //제목 또는 메모에 입력값이 없으면 게시되지않도록
                if (memo.trim().length() == 0 || Title.trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "메모 또는 제목이 작성되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                //출발지, 목적지, 제한인원을 설정하지 않으면 게시되지않도록
                else if (RadioButton_departuresunmoon.isChecked() == false && RadioButton_departureasan.isChecked() == false && RadioButton_departurecheonan.isChecked() == false && RadioButton_departuretra.isChecked() == false && RadioButton_departureterminal.isChecked() == false){
                    Toast.makeText(getApplicationContext(), "출발지를 설정해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (RadioButton_arrivalsunmoon.isChecked() == false && RadioButton_arrivalasan.isChecked() == false && RadioButton_arrivalcheonan.isChecked() == false && RadioButton_arrivaltra.isChecked() == false && RadioButton_arrivalterminal.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "목적지를 설정해주세요", Toast.LENGTH_SHORT).show();
                }
                else if (RadioButton_one.isChecked() == false && RadioButton_two.isChecked() == false && RadioButton_three.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "제한인원을 설정해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    //String memoData = content.getText().toString();
                    //memofile.save(memoData);
                    //content.setText("");
                    //contents = contents.replace("'","''");//db에 저장할때 작은따옴표가 문자로 인식안해서 오류, 문자로 읽을수있도록 바꿔줌.

                    intent.putExtra("Title", Title);
                    intent.putExtra("arrival", arrival);
                    intent.putExtra("departure", departure);
                    intent.putExtra("person", person);
                    intent.putExtra("memo", memo);
                    //intent.putExtra("memoData",memoData);

                    startActivity(intent); //액티비티 이동
                    finish(); //저장버튼 클릭시 작성 액티비티 날리기
                    Toast.makeText(getApplicationContext(), "게시 되었습니다.", Toast.LENGTH_SHORT).show(); //게시판으로 넘어갔을때 게시되었습니다. 토스트메세지 띄어줌
                }

            }
        });

        Button_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_join = new Intent(PostingActivity.this, Posting_join.class);

                Title = TextInputEditText.getText().toString(); //버튼을 눌렀을때 데이터가 들어간채로 넘어갈수잇게
                memo = EditText_memo.getText().toString(); //메모텍스트 저장

                //작성자 제외 다른 사람들이 들어갈 게시글로 넘기는 동작.
                intent_join.putExtra("Title_join", Title);
                intent_join.putExtra("arrival_join", arrival);
                intent_join.putExtra("departure_join",departure);
                intent_join.putExtra("person_join",person_join);
                intent_join.putExtra("memo_join",memo);

                startActivity(intent_join);
            }
        });
    }

    //라디오버튼 클릭 이벤트. 선택했을때 동작
    RadioGroup.OnCheckedChangeListener m = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            Log.v("출력", i + "");

            CameraUpdate cameraUpdate = null;

            //출발지 라디오버튼 리스너
            if (radioGroup.getId() == R.id.RadioGroup_departure) {
                if (i == R.id.RadioButton_departuresunmoon){
                    marker.setIcon(MarkerIcons.BLACK);
                    marker.setIconTintColor(Color.RED);

                    marker1.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_arrivalsunmoon.setEnabled(false);//출발지와 도착지를 같은 장소 선택 못하도록
                    RadioButton_arrivalcheonan.setEnabled(true);
                    RadioButton_arrivalasan.setEnabled(true);
                    RadioButton_arrivaltra.setEnabled(true);
                    RadioButton_arrivalterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.799218, 127.074920)).animate(CameraAnimation.Easing);
                    //cameraUpdate = CameraUpdate.scrollAndZoomTo(new LatLng(36.799218, 127.074920), 15);

                    //지도 출발지, 목적지 버튼 클릭시 그의 맞는 좌표값으로 이동하는 메소드.
                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });

                    departure = ((RadioButton)findViewById(R.id.RadioButton_departuresunmoon)).getText().toString(); //클릭시 텍스트값 넘기기위해
                }
                else if (i == R.id.RadioButton_departurecheonan) {
                    marker1.setIcon(MarkerIcons.BLACK);
                    marker1.setIconTintColor(Color.RED);

                    marker.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_arrivalsunmoon.setEnabled(true);//출발지와 도착지를 같은 장소 선택 못하도록
                    RadioButton_arrivalcheonan.setEnabled(false);
                    RadioButton_arrivalasan.setEnabled(true);
                    RadioButton_arrivaltra.setEnabled(true);
                    RadioButton_arrivalterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.809299, 127.146593)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    departure = ((RadioButton)findViewById(R.id.RadioButton_departurecheonan)).getText().toString();
                }
                else if (i == R.id.RadioButton_departuretasan) {
                    marker2.setIcon(MarkerIcons.BLACK);
                    marker2.setIconTintColor(Color.RED);

                    marker1.setIconTintColor(0);
                    marker.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_arrivalsunmoon.setEnabled(true);//출발지와 도착지를 같은 장소 선택 못하도록
                    RadioButton_arrivalcheonan.setEnabled(true);
                    RadioButton_arrivalasan.setEnabled(false);
                    RadioButton_arrivaltra.setEnabled(true);
                    RadioButton_arrivalterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.792171, 127.104496)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    departure = ((RadioButton)findViewById(R.id.RadioButton_departuretasan)).getText().toString();
                }
                else if (i == R.id.RadioButton_departuretra) {
                    marker3.setIcon(MarkerIcons.BLACK);
                    marker3.setIconTintColor(Color.RED);

                    marker1.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_arrivalsunmoon.setEnabled(true);//출발지와 도착지를 같은 장소 선택 못하도록
                    RadioButton_arrivalcheonan.setEnabled(true);
                    RadioButton_arrivalasan.setEnabled(true);
                    RadioButton_arrivaltra.setEnabled(false);
                    RadioButton_arrivalterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.798487, 127.060894)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    departure = ((RadioButton)findViewById(R.id.RadioButton_departuretra)).getText().toString();
                }
                else if (i == R.id.RadioButton_departureterminal) {
                    marker4.setIcon(MarkerIcons.BLACK);
                    marker4.setIconTintColor(Color.RED);

                    marker1.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker.setIconTintColor(0);

                    RadioButton_arrivalsunmoon.setEnabled(true);//출발지와 도착지를 같은 장소 선택 못하도록
                    RadioButton_arrivalcheonan.setEnabled(true);
                    RadioButton_arrivalasan.setEnabled(true);
                    RadioButton_arrivaltra.setEnabled(true);
                    RadioButton_arrivalterminal.setEnabled(false);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.820818, 127.156362)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    departure = ((RadioButton)findViewById(R.id.RadioButton_departureterminal)).getText().toString();
                }
            }
            //Log.d("카메라 업데이트 에러", cameraUpdate.toString());
            //NaverMap.moveCamera(cameraUpdate);
            //naverMap.moveCamera(cameraUpdate);

            //목적지 라디오버튼 리스너
            if (radioGroup.getId() == R.id.RadioGroup_arrival) {
                if (i == R.id.RadioButton_arrivalsunmoon){
                    marker.setIcon(MarkerIcons.BLACK);
                    marker.setIconTintColor(Color.YELLOW);

                    marker1.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_departuresunmoon.setEnabled(false);
                    RadioButton_departurecheonan.setEnabled(true);
                    RadioButton_departureasan.setEnabled(true);
                    RadioButton_departuretra.setEnabled(true);
                    RadioButton_departureterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.799218, 127.074920)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    arrival = ((RadioButton)findViewById(R.id.RadioButton_arrivalsunmoon)).getText().toString();
                }
                else if (i == R.id.RadioButton_arrivalcheonan) {
                    marker1.setIcon(MarkerIcons.BLACK);
                    marker1.setIconTintColor(Color.YELLOW);

                    marker.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_departuresunmoon.setEnabled(true);
                    RadioButton_departurecheonan.setEnabled(false);
                    RadioButton_departureasan.setEnabled(true);
                    RadioButton_departuretra.setEnabled(true);
                    RadioButton_departureterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.809299, 127.146593)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    arrival = ((RadioButton)findViewById(R.id.RadioButton_arrivalcheonan)).getText().toString();
                }
                else if (i == R.id.RadioButton_arrivalasan) {
                    marker2.setIcon(MarkerIcons.BLACK);
                    marker2.setIconTintColor(Color.YELLOW);

                    marker1.setIconTintColor(0);
                    marker.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_departuresunmoon.setEnabled(true);
                    RadioButton_departurecheonan.setEnabled(true);
                    RadioButton_departureasan.setEnabled(false);
                    RadioButton_departuretra.setEnabled(true);
                    RadioButton_departureterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.792171, 127.104496)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    arrival = ((RadioButton)findViewById(R.id.RadioButton_arrivalasan)).getText().toString();
                }
                else if (i == R.id.RadioButton_arrivaltra) {
                    marker3.setIcon(MarkerIcons.BLACK);
                    marker3.setIconTintColor(Color.YELLOW);

                    marker1.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker.setIconTintColor(0);
                    marker4.setIconTintColor(0);

                    RadioButton_departuresunmoon.setEnabled(true);
                    RadioButton_departurecheonan.setEnabled(true);
                    RadioButton_departureasan.setEnabled(true);
                    RadioButton_departuretra.setEnabled(false);
                    RadioButton_departureterminal.setEnabled(true);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.798487, 127.060894)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    arrival = ((RadioButton)findViewById(R.id.RadioButton_arrivaltra)).getText().toString();
                }
                else if (i == R.id.RadioButton_arrivalterminal) {
                    marker4.setIcon(MarkerIcons.BLACK);
                    marker4.setIconTintColor(Color.YELLOW);

                    marker1.setIconTintColor(0);
                    marker2.setIconTintColor(0);
                    marker3.setIconTintColor(0);
                    marker.setIconTintColor(0);

                    RadioButton_departuresunmoon.setEnabled(true);
                    RadioButton_departurecheonan.setEnabled(true);
                    RadioButton_departureasan.setEnabled(true);
                    RadioButton_departuretra.setEnabled(true);
                    RadioButton_departureterminal.setEnabled(false);

                    cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.820818, 127.156362)).animate(CameraAnimation.Easing);

                    CameraUpdate finalCameraUpdate = cameraUpdate;
                    mapView.getMapAsync(naverMap1 -> {
                        naverMap1.moveCamera((finalCameraUpdate));
                    });


                    arrival = ((RadioButton)findViewById(R.id.RadioButton_arrivalterminal)).getText().toString();
                }
            }
            //제한인원 버튼 리스너
            if (radioGroup.getId() == R.id.RadioGroup_person) {
                if (i == R.id.RadioButton_one) {
                    person = ((RadioButton) findViewById(R.id.RadioButton_one)).getText().toString();
                    person_join = ((RadioButton) findViewById(R.id.RadioButton_one)).getText().toString();
                }
                else if (i == R.id.RadioButton_two) {
                    person = ((RadioButton) findViewById(R.id.RadioButton_two)).getText().toString();
                    person_join = ((RadioButton) findViewById(R.id.RadioButton_two)).getText().toString();
                }
                else if (i == R.id.RadioButton_three) {
                    person = ((RadioButton) findViewById(R.id.RadioButton_three)).getText().toString();
                    person_join = ((RadioButton) findViewById(R.id.RadioButton_three)).getText().toString();
                }
            }
        }
    };

    //네이버 지도
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        marker.setPosition(new LatLng(36.799218, 127.074920));

        marker.setOnClickListener(overlay -> { //마크 클릭시 출발지 목적지 선택 가능하도록.
            Toast.makeText(getApplicationContext(), "선문대학교클릭",Toast.LENGTH_SHORT).show();
            return true;
        });
        marker1.setPosition(new LatLng(36.809299, 127.146593));
        marker1.setOnClickListener(overlay -> { //마크 클릭시 출발지 목적지 선택 가능하도록.
            Toast.makeText(getApplicationContext(), "천안역클릭",Toast.LENGTH_SHORT).show();
            return true;
        });
        marker2.setPosition(new LatLng(36.792171, 127.104496));
        marker2.setOnClickListener(overlay -> { //마크 클릭시 출발지 목적지 선택 가능하도록.
            Toast.makeText(getApplicationContext(), "아산역클릭",Toast.LENGTH_SHORT).show();
            return true;
        });
        marker3.setPosition(new LatLng(36.798487, 127.060894));
        marker3.setOnClickListener(overlay -> { //마크 클릭시 출발지 목적지 선택 가능하도록.
            Toast.makeText(getApplicationContext(), "트라팰리스클릭",Toast.LENGTH_SHORT).show();
            return true;
        });
        marker4.setPosition(new LatLng(36.820818, 127.156362));
        marker4.setOnClickListener(overlay -> { //마크 클릭시 출발지 목적지 선택 가능하도록.
            Toast.makeText(getApplicationContext(), "천안터미널클릭",Toast.LENGTH_SHORT).show();
            return true;
        });
        marker.setMap(naverMap);
        marker1.setMap(naverMap);
        marker2.setMap(naverMap);
        marker3.setMap(naverMap);
        marker4.setMap(naverMap);

        //naverMap.setSymbolScale(0.1f);//심볼크기 조절
        naverMap.setLightness(0.1f);//지도 밝기
        //LatLng l = new LatLng(36.820818, 127.156362);

        //naverMap.setMapType(NaverMap.MapType.Hybrid); //지도 유형
    }
}
