package forsubject; 
import java.util.Scanner; 
public class test {
static Scanner scan=new Scanner(System.in);

    public static void Inverted() {
    System.out.print("삼각형 크기 : ");
				int num = scan.nextInt();
    String str = "*";

			    	for(int i = 0; i < num; i++) {
      	     	for(int j = 0; j < num- (i + 1); j++) {
                   System.out.print(" ");
     			     }
             for(int j=0; j<= 1 + 2 * i; j++) {
                   System.out.print(str);
             }
     			     System.out.println();
      		}
     }
       	public static void main(String[] args) {
        Inverted();
     	}

 }
