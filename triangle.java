package forsubject;
//import java.util.Scanner;
public class Triangle {

	private int layer;
	private String shape;

  public Triangle(int layer, String shape){
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
