package main;

import java.util.Random;

public class Runner {
	public static void main(String[] args) { //imagine we can condition on which problem they do and pipe in corresponding parsing....
		HideSeekParser MaxWritesThis = new HideSeekParser();
		forestSquare[][] board = MaxWritesThis.makeBoard("sampleForest.txt");
		System.out.println("made board");
		printGrid(board);
		System.out.println("randomly placing friends");
		randomlyInsertFriends(board);
		System.out.println();
		System.out.println();
		System.out.println();
		printGrid(board);
	}
	//RUNS THE RISK OF SMASHING INTO A TREE FOREVER
	public static void randomlyInsertFriends(forestSquare[][] forest){
		for (forestSquare[] forestSquares : forest) {
			int row = forestSquares[0].row;
			for (forestSquare forestSquare : forestSquares) {
				while(true){
					Random R= new Random();
					int col=R.nextInt(forestSquares.length-1);
					if(forest[row][col].value=='E'){
						System.out.println("found one!");
						forest[row][col].value='F';
						break;
					}
				}break;
			}
		}
	}
	public static void printGrid(forestSquare[][] b)
	{
	   for(int i = 0; i < b.length; i++)
	   {
	      for(int j = 0; j < b[0].length; j++)
	      {
	         System.out.print(b[i][j].value);
	      }
	      System.out.println();
	   }
	}
}
