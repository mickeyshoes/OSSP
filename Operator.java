/*�Ͻ� : 19.04.23
 * ������ : �Ӽ���
 * ���߸��� : ���������� ����� ��� �����
 */
package forsubject;
import java.util.Scanner;
public class Operator{

    public static void main(String[] args){
      Scanner scan = new Scanner(System.in);

      while(true){
        System.out.println("����� ���α׷�");
        System.out.println("1.������ 2.�𷡽ð� 3.����");
        int puts = scan.nextInt(); // ��缱���� ����
        if(puts==3) { // 3�� �Է½� �ݺ��� Ż��
          System.out.println("������ּż� �����մϴ�.");
          break;
        }
        System.out.println("�� ���� ���� �Է� :");
        int layer = scan.nextInt(); // ���� ���� �Է¹���
        System.out.println("��� ����");
        System.out.println("1.�� 2.��");
        int choose = scan.nextInt();
        String shape; // ����ڰ� �� ���ڸ� ������ ������ �Ű������� �Ѱ��ֱ� ����
        if(choose ==1){
            shape = "��";
        }
          else{
            shape = "��";
          }
        Triangle tr = new Triangle(layer,shape);
        InvertTriangle itr = new InvertTriangle(layer,shape);

        if(puts ==1){ // ���ﰢ��-�Ʒ��ﰢ�� ������ ���
          tr.Triangle_f();
          itr.I_Triangle();
        }
        else if(puts==2){ // �Ʒ��ﰢ��-���ﰢ�� ������ ���
          itr.I_Triangle();
          System.out.println("");
          tr.Triangle_f();
        }
        System.out.println("");
      }

    }


}
