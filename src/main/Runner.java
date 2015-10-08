package main;

import java.util.Random;

public class Runner {
	static forestSquare[] friends;
	public static void main(String[] args) { //imagine we can condition on which problem they do and pipe in corresponding parsing....
		HideSeekParser MaxWritesThis = new HideSeekParser();
		forestSquare[][] board = MaxWritesThis.makeBoard(args[0]);
		//System.out.println("made board");
		friends=new forestSquare[board.length];
		//printGrid(board);
		//System.out.println();
		//System.out.println();
		System.out.println("Randomly placing friends:");
		randomlyInsertFriends(board);
		forestSquare[][] board2=deepclone(board);
		forestSquare[][] board3 =deepclone(board);
		//DO NOT DEEPFRIENDS()
		forestSquare[] friends2 = new forestSquare[friends.length];//deepFriends(friends);
		forestSquare[] friends3 = new forestSquare[friends.length];//deepFriends(friends);
		for(int i=0;i<friends.length;i++){
			friends2[i]=board2[friends[i].row][friends[i].col];
			friends3[i]=board3[friends[i].row][friends[i].col];
		}
		printGrid(board);
		printFriends(board);
		System.out.println();
		System.out.println("Choosing Friends to move in random manner");
		//System.out.println();
		LocalSearcher L= new LocalSearcher(board,friends);
		L.searchRand();
		printGrid(L.board);
		printFriends(L.board);
		//this gets stuck
		System.out.println();
		System.out.println("Choosing Friends to move in min-conflict-first manner");
		//System.out.println();
		///////////////////////
		//randomlyInsertFriends(board2);
		LocalSearcher L2=new LocalSearcher(board2,friends2);
		L2.searchMin();
		printGrid(L2.board);
		printFriends(L2.board);
		//this also gets stuck
		System.out.println();
		System.out.println("Choosing Friends to move in max-conflict-first manner");
		//System.out.println();
		//randomlyInsertFriends(board3);
		LocalSearcher L3=new LocalSearcher(board3,friends3);
		L3.searchMax();
		printGrid(L3.board);
		printFriends(L3.board);
		
		
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
						forest[row][col].value='F';
						friends[row]=forest[row][col];
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
	public static forestSquare[][] deepclone(forestSquare[][] orig){
		forestSquare[][] returnable = new forestSquare[orig.length][orig.length];
		for(int i=0;i<orig.length;i++){
			for(int j=0;j<orig.length;j++){
				returnable[i][j]=new forestSquare(orig[i][j].row,orig[i][j].col,orig[i][j].value);
			}
		}
		return returnable;
	}
	public static forestSquare[] deepFriends(forestSquare[] orig){
		forestSquare[] returnable = new forestSquare[orig.length];
		for(int i=0;i<orig.length;i++){
			returnable[i]=new forestSquare(orig[i].row,orig[i].col,orig[i].value);
		}
		return returnable;
	}
	public static void printFriends(forestSquare[][] board){
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[0].length;j++){
				if(board[i][j].value=='F')
					System.out.println((i+1)+" "+(j+1));
			}
		}
	}
}
