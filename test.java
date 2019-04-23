package forsubject;

import java.util.Scanner;

public class test {
	static Scanner scan = new Scanner(System.in);

	public static void Inverted() {
		System.out.print("���� �ﰢ�� ũ��:");

		int n = scan.nextInt();//�ﰢ�� ũ�� ����.
		String nemo = "��";

		for(int i = 1; i<=n; i++) { // �����ڰ� �Է��� ������ ����
			for(int j = 0; j<=(i-1); j++) { //  n���� �ϳ��� �ٿ����� ��ĭ�� ����������
				System.out.print(" ");
			}

			for(int j=0; j<(2*n)-(2*i-1); j++) { //�Է¼����� �ϳ��� �ٿ����鼭 ����
				System.out.print(nemo);
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		Inverted();
	}

}
