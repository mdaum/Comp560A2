package main;

import java.util.ArrayList;
import java.util.Random;

public class LocalSearcher {
	static forestSquare[][] board;
	static forestSquare[] friends;
	static ArrayList<forestSquare> conflictF;
	public LocalSearcher(forestSquare[][] board,forestSquare[]friends){
		this.board=board;
		this.friends=friends;
		this.conflictF=new ArrayList<forestSquare>();
	}
	public static void searchMin(){
		int runs=0;
		for (forestSquare friend : friends) {
			int c = conflicts(friend);
			if(c>0)conflictF.add(friend);
		}
		forestSquare chosenOne;
		int tempConflictCount;
		int bestConflictCount = Integer.MAX_VALUE;
		int bestCol=0;
		while(!conflictF.isEmpty()){
			chosenOne=null;
			int bestCon=Integer.MAX_VALUE;
			for (forestSquare doody : conflictF) {
				int c =conflicts(doody);
				if(c<bestCon){
					chosenOne=doody;
					bestCon=c;
				}
			}
			runs++;
			System.out.println();
			Runner.printGrid(board);
			conflictF.remove(chosenOne);
			chosenOne.value='E';//to make sure that this friend doesn't conflict with itself
			for(int i=0;i<board.length;i++){
				if(board[chosenOne.row][i].value=='T')
					continue;
				tempConflictCount=conflicts(board[chosenOne.row][i]);
				if(tempConflictCount<bestConflictCount){
					bestCol=i;
					bestConflictCount=tempConflictCount;
				}
			}
			board[chosenOne.row][bestCol].value='F';
			if(bestConflictCount!=0){
				conflictF.add(board[chosenOne.row][bestCol]);
				ArrayList<forestSquare> problemFriends = conflictingFriends(board[chosenOne.row][bestCol]);
				for(forestSquare conflictingFriend : problemFriends){
					if(!conflictF.contains(conflictingFriend))
						conflictF.add(conflictingFriend);
				}
			}
			//RESET DESE PROBLEM CHILLUNS
			bestCol=0;
			bestConflictCount=Integer.MAX_VALUE;
		}
		System.out.println("Finished after moving "+runs+" friends.");
		//we're done
	}
	public static void searchMax(){
		int runs=0;
		for (forestSquare friend : friends) {
			int c = conflicts(friend);
			if(c>0)conflictF.add(friend);
		}
		forestSquare chosenOne;
		int tempConflictCount;
		int bestConflictCount = Integer.MAX_VALUE;
		int bestCol=0;
		while(!conflictF.isEmpty()){
			chosenOne=null;
			int bestCon=Integer.MIN_VALUE;
			for (forestSquare doody : conflictF) {
				int c =conflicts(doody);
				if(c>bestCon){
					chosenOne=doody;
					bestCon=c;
				}
			}
			runs++;
			conflictF.remove(chosenOne);
			chosenOne.value='E';//to make sure that this friend doesn't conflict with itself
			for(int i=0;i<board.length;i++){
				if(board[chosenOne.row][i].value=='T')
					continue;
				tempConflictCount=conflicts(board[chosenOne.row][i]);
				if(tempConflictCount<bestConflictCount){
					bestCol=i;
					bestConflictCount=tempConflictCount;
				}
			}
			board[chosenOne.row][bestCol].value='F';
			if(bestConflictCount!=0){
				conflictF.add(board[chosenOne.row][bestCol]);
				ArrayList<forestSquare> problemFriends = conflictingFriends(board[chosenOne.row][bestCol]);
				for(forestSquare conflictingFriend : problemFriends){
					if(!conflictF.contains(conflictingFriend))
						conflictF.add(conflictingFriend);
				}
			}
			//RESET DESE PROBLEM CHILLUNS
			bestCol=0;
			bestConflictCount=Integer.MAX_VALUE;
		}
		//we're done
		System.out.println("Finished after moving "+runs+" friends.");
	}
	public static void searchRand(){
		int runs=0;
		Random R = new Random();
		for (forestSquare friend : friends) {
			int c = conflicts(friend);
			if(c>0)conflictF.add(friend);
		}
		forestSquare chosenOne;
		int tempConflictCount;
		int bestConflictCount = Integer.MAX_VALUE;
		int bestCol=0;
		while(!conflictF.isEmpty()){
			chosenOne = conflictF.remove(R.nextInt(conflictF.size()));
			runs++;
			chosenOne.value='E';//to make sure that this friend doesn't conflict with itself
			for(int i=0;i<board.length;i++){
				if(board[chosenOne.row][i].value=='T')
					continue;
				tempConflictCount=conflicts(board[chosenOne.row][i]);
				if(tempConflictCount<bestConflictCount){
					bestCol=i;
					bestConflictCount=tempConflictCount;
				}
			}
			board[chosenOne.row][bestCol].value='F';
			if(bestConflictCount!=0){
				conflictF.add(board[chosenOne.row][bestCol]);
				ArrayList<forestSquare> problemFriends = conflictingFriends(board[chosenOne.row][bestCol]);
				for(forestSquare conflictingFriend : problemFriends){
					if(!conflictF.contains(conflictingFriend))
						conflictF.add(conflictingFriend);
				}
			}
			//RESET DESE PROBLEM CHILLUNS
			bestCol=0;
			bestConflictCount=Integer.MAX_VALUE;
		}
		//we're done
		System.out.println("Finished after moving "+runs+" friends.");
	}
	public static ArrayList<forestSquare> conflictingFriends(forestSquare square){
		ArrayList<forestSquare> problemFriends = new ArrayList<forestSquare>();
		//the plan is to pick a direction, iterate in that direction until we hit a tree
		//or a grid boundary, and increment conflictCount for each person we hit along the way
		//check NW+SE
		problemFriends.addAll(conflictingFriendsInDirection(square,-1,-1));
		problemFriends.addAll(conflictingFriendsInDirection(square,1,1));
		//check SW+NE
		problemFriends.addAll(conflictingFriendsInDirection(square,-1,1));
		problemFriends.addAll(conflictingFriendsInDirection(square,1,-1));
		//check left+right
		problemFriends.addAll(conflictingFriendsInDirection(square,-1,0));
		problemFriends.addAll(conflictingFriendsInDirection(square,1,0));
		//check up+down
		problemFriends.addAll(conflictingFriendsInDirection(square,0,-1));
		problemFriends.addAll(conflictingFriendsInDirection(square,0,1));
		//System.out.println(square.row+","+square.col+" has "+problemFriends.size()+" conflicts");
		return problemFriends;
	}
	public static ArrayList<forestSquare> conflictingFriendsInDirection(forestSquare square, int xDirection, int yDirection){
		ArrayList<forestSquare> problemFriends = new ArrayList<forestSquare>();
		int row=square.row+xDirection;
		int col=square.col+yDirection;
		while(row>=0 && col>=0 && row<board.length &&col<board.length){
			if(board[row][col].value=='T')
				break;
			else if(board[row][col].value=='F')
				problemFriends.add(board[row][col]);
			row+=xDirection;
			col+=yDirection;
		}
		return problemFriends;
	}
	public static int conflicts(forestSquare square){
		int conflictCount=0;
		//the plan is to pick a direction, iterate in that direction until we hit a tree
		//or a grid boundary, and increment conflictCount for each person we hit along the way
		//check NW+SE
		conflictCount+=conflictsInDirection(square,-1,-1);
		conflictCount+=conflictsInDirection(square,1,1);
		//check SW+NE
		conflictCount+=conflictsInDirection(square,-1,1);
		conflictCount+=conflictsInDirection(square,1,-1);
		//check left+right
		conflictCount+=conflictsInDirection(square,-1,0);
		conflictCount+=conflictsInDirection(square,1,0);
		//check up+down
		conflictCount+=conflictsInDirection(square,0,-1);
		conflictCount+=conflictsInDirection(square,0,1);
		//System.out.println(square.row+","+square.col+" has "+conflictCount+" conflicts");
		return conflictCount;
	}
	public static int conflictsInDirection(forestSquare square, int xDirection, int yDirection){
		int conflictCount=0;
		int row=square.row+xDirection;
		int col=square.col+yDirection;
		while(row>=0 && col>=0 && row<board.length &&col<board.length){
			if(board[row][col].value=='T')
				break;
			else if(board[row][col].value=='F')
				conflictCount++;
			row+=xDirection;
			col+=yDirection;
		}
		return conflictCount;
	}
	
}
