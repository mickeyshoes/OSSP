
//import java.util.Scanner;
public class triangle {

	private int layer; // 변의길이
	private String shape; // 모양

  public triangle(int layer, String shape){ // 변의길의와 모양을 입력받아 필드값으로 저장
	     this.layer = layer;
	     this.shape = shape;
}

  public void Triangle_f() {

    for(int i = 0; i < layer; i++) {
          for(int j = 0; j < layer- (i + 1); j++) {
              System.out.print("   ");
          }
          for(int j=0; j< 1 + 2 * i; j++) {
              System.out.print(shape);
            }
            if(i==layer) {
              break;
            }
            else {
              System.out.println("");
            }
   }
  }
}
