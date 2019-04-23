package forsubject;
import java.util.Scanner;
public class Operator{

    public static void main(String[] args){
      Scanner scan = new Scanner(System.in);

      while(true){
        System.out.println("별찍기 프로그램");
        System.out.println("1.마름모 2.모래시계 3.종료");
        int puts = scan.nextInt();
        if(puts==3) {
          System.out.println("사용해주셔서 감사합니다.");
          break;
        }
        System.out.println("층수 입력 :");
        int layer = scan.nextInt();
        System.out.println("모양 선택");
        System.out.println("1.★ 2.☆");
        int choose = scan.nextInt();
        String shape;
        if(choose ==1){
            shape = "★";
        }
          else{
            shape = "☆";
          }
        Triangle tr = new Triangle(layer,shape);
        InvertTriangle itr = new InvertTriangle(layer,shape);

        if(puts ==1){
          tr.Triangle_f();
          itr.I_Triangle();
        }
        else if(puts==2){
          itr.I_Triangle();
          System.out.println("");
          tr.Triangle_f();
        }
        System.out.println("");
      }

    }


}
