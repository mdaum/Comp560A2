package main;

public class LocalSearcher {
	forestSquare[][] board;
	public LocalSearcher(forestSquare[][] board){
		this.board=board;
	}
	public int conflicts(forestSquare square){
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
		return conflictCount;
	}
	public int conflictsInDirection(forestSquare square, int xDirection, int yDirection){
		int conflictCount=0;
		int row=square.row+xDirection;
		int col=square.col+yDirection;
		while(row>=0 && col>=0 && row<board.length){
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
