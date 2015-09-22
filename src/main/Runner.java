package main;

public class Runner {
	public static void main(String[] args) { //imagine we can condition on which problem they do and pipe in corresponding parsing....
		HideSeekParser MaxWritesThis = new HideSeekParser();
		forestSquare[][] board = MaxWritesThis.makeBoard("sampleForest.txt");
		System.out.println("made board");
		printGrid(board);

	}
	
	
	
	public static void printGrid(forestSquare[][] b)
	{
	   for(int i = 0; i < b.length; i++)
	   {
	      for(int j = 0; j < b[0].length; j++)
	      {
	         System.out.print(b[i][j].domain);
	      }
	      System.out.println();
	   }
	}
}
