package forsubject;

//import java.util.Scanner;
public class InvertTriangle {
	//static Scanner scan = new Scanner(System.in);
	private int layer;// 변의 길이
	private String shape;// 모양

	public InvertTriangle(int layer, String shape){// 변의 길이와 모양을 입력받아 필드값으로 저장
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
