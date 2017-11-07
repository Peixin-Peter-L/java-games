import java.util.Arrays;

public class RotationTest {

	public static void main(String[] args) {
		int[][] begin = {
			{1,2,3},
			{4,5,6},
			{7,8,9}
		};
		print(begin);
		int[][] temp = new int[begin.length][begin[0].length];
		for(int i=0;i<begin.length;i++){
			for(int j=0;j<begin[0].length;j++){
				temp[i][j] = begin[2-j][i];
			}
		}
		print(temp);
	}
	
	public static void print(int[][] arr){
		System.out.println("--------------------------------------------");
		for(int[] a:arr){
			System.out.println(Arrays.toString(a));
		}
	}

}
