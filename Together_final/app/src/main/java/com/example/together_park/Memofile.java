//package com.example.together_park;
//
//import android.content.Context;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class Memofile {
//    private static final String FILE_NAME = "Memo.txt";
//
//    //메모 내용을 저장할 파일 이름.
//    Context mContext = null;
//
//    public Memofile(Context context) {
//        mContext = context;
//    }
//
//    //저장하는 메소드
//    public void save(String strData) {
//        if(strData == null || strData.equals("")) {
//            return;
//        }
//
//        FileOutputStream fosMemo = null;
//
//        try {
//            //파일에 데이터 쓰기 위한 output 스트림 생성.
//            fosMemo = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//            //파일에 적기
//            fosMemo.write(strData.getBytes());
//            fosMemo.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //저장된 함수 가져오는 메소드
//    public String load() {
//        try {
//            //데이터 읽기 위한 input스트림 생성
//            FileInputStream fisMemo = mContext.openFileInput(FILE_NAME);
//            //데이터를 읽고 타입 변환 byte -> string
//            byte[]memoData = new byte[fisMemo.available()];
//
//            while(fisMemo.read(memoData) != -1) {}
//            return new String(memoData);
//        } catch (IOException e) {
//
//        }
//        return "";
//    }
//}
