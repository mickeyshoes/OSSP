package forsubject;

import java.util.Scanner;

public class test {
	static Scanner scan = new Scanner(System.in);
	
	public static void Inverted() {
		System.out.print("위에 삼각형 크기:");
		
		int n = scan.nextInt();//삼각형 크기 결정.
		String nemo = "■";
		
		for(int i = 1; i<=n; i++) { // 사용자가 입력한 수까지 출력
			for(int j = 0; j<=(i-1); j++) { //  n개를 하나씩 줄여가며 한칸씩 띄어지도록
				System.out.print(" ");
			}
			
			for(int j=0; j<(2*n)-(2*i-1); j++) { //입력수에서 하나씩 줄여가면서 출력
				System.out.print(nemo);
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		Inverted();
	}

}
