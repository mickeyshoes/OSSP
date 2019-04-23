package forsubject;

//import java.util.Scanner;
public class InvertTriangle {
	//static Scanner scan = new Scanner(System.in);
	private int layer;
	private String shape;

	public InvertTriangle(int layer, String shape){
		this.layer = layer;
		this.shape = shape;
}

	public void I_Triangle() {

		for(int i = 1; i<=layer; i++) { // �����ڰ� �Է��� ������ ����
			for(int j = 0; j<(i-1); j++) { //  n���� �ϳ��� �ٿ����� ��ĭ�� ����������
				System.out.print("   ");
			}

			for(int j=0; j<(2*layer)-(2*i-1); j++) { //�Է¼����� �ϳ��� �ٿ����鼭 ����
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
