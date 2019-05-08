/*일시 : 19.04.23
 * 개발자 : 임성민
 * 개발목적 : 이중포문을 사용한 모양 만들기
 */
package forsubject;
import java.util.Scanner;
public class Operator{

    public static void main(String[] args){
      Scanner scan = new Scanner(System.in);

      while(true){
        System.out.println("별찍기 프로그램");
        System.out.println("1.마름모 2.모래시계 3.종료");
        int puts = scan.nextInt(); // 모양선택을 받음
        if(puts==3) { // 3번 입력시 반복문 탈출
          System.out.println("사용해주셔서 감사합니다.");
          break;
        }
        System.out.println("한 변의 길이 입력 :");
        int layer = scan.nextInt(); // 변의 길이 입력받음
        System.out.println("모양 선택");
        System.out.println("1.★ 2.☆");
        int choose = scan.nextInt();
        String shape; // 사용자가 고른 문자를 생성자 생성시 매개값으로 넘겨주기 위함
        if(choose ==1){
            shape = "★";
        }
          else{
            shape = "☆";
          }
        triangle tr = new triangle(layer,shape);
        test itr = new test(layer,shape);

        if(puts ==1){ // 위삼각형-아래삼각형 순으로 출력
          tr.Triangle_f();
          itr.I_Triangle();
        }
        else if(puts==2){ // 아래삼각형-위삼각형 순으로 출력
          itr.I_Triangle();
          System.out.println("");
          tr.Triangle_f();
        }
        System.out.println("");
      }

    }


}
