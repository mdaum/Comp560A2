package a3main;

import java.util.ArrayList;

public class MinimaxSearch 
{
	public int count = 0;
	private Player blue,green;
	public ArrayList<ArrayList<CandyNode>> board;
	private boolean blueTurn;
	
	public char maxPlayer;
	public char minPlayer;
	
	public MinimaxSearch(Player blue, Player green, ArrayList<ArrayList<CandyNode>> board)
	{
		this.blue = blue;
		this.green = green;
		this.board = board;
		this.blueTurn = true;
	}
	
	public ArrayList<ArrayList<CandyNode>> depthLimitedSearch(int depth)
	{
		System.out.println("Entering search...");
		while(!gameOver(board))
		{
			ArrayList<ArrayList<CandyNode>> boardState = deepCloneBoard(board);
			if(blueTurn)
			{
				maxPlayer = blue.getName();
				minPlayer = green.getName();
			}
			else
			{
				maxPlayer = green.getName();
				minPlayer = blue.getName();
			}
			CandyNode decision = minimaxDecision(boardState,depth);
			System.out.println("Decision to make move at " + decision.getColumn() + " , " + decision.getRow());
			
			board.get(decision.getRow()).get(decision.getColumn()).setOwner(maxPlayer);
			blueTurn = !blueTurn;
		}
		return null;
	}
	
	public void playOneMove(int depth)
	{
		System.out.println("Entering search...");
		ArrayList<ArrayList<CandyNode>> boardState = deepCloneBoard(board);
		if(blueTurn)
		{
			maxPlayer = blue.getName();
			minPlayer = green.getName();
		}
		else
		{
			maxPlayer = green.getName();
			minPlayer = blue.getName();
		}
		CandyNode decision = minimaxDecision(boardState,depth);
		System.out.println("Decision to make move at " + decision.getColumn() + " , " + decision.getRow());
		if(decision != null)
		{
			board.get(decision.getRow()).get(decision.getColumn()).setOwner(maxPlayer);
			if(adjacentFriendlyCandy(board,board.get(decision.getRow()).get(decision.getColumn())))
			{
				System.out.println("Infecting...");
				infectEnemyCandy(board,board.get(decision.getRow()).get(decision.getColumn()));
			}
		}
		blueTurn = !blueTurn;
	}

	
	public CandyNode minimaxDecision(ArrayList<ArrayList<CandyNode>> boardState,int depth)
	{
		System.out.println(maxPlayer);
		CandyNode chosenOne = null;

		int maxVal = Integer.MIN_VALUE;
		for(int i = 0;i < board.size();i++)
		{
			for(int j = 0;j < board.get(0).size();j++)
			{
				CandyNode action = boardState.get(i).get(j);
				if(action.getOwner() == '\0')
				{
					ArrayList<ArrayList<CandyNode>> nextBoardState = deepCloneBoard(boardState);
					nextBoardState.get(i).get(j).setOwner(maxPlayer);
					if(adjacentFriendlyCandy(nextBoardState,nextBoardState.get(i).get(j)))
					{
						infectEnemyCandy(nextBoardState,nextBoardState.get(i).get(j));
					}
					
					int minVal = minValue(nextBoardState,depth);
					if(maxVal < minVal)
					{
						maxVal = minVal;
						chosenOne = boardState.get(i).get(j);
					}
				}
			}
		}
		return chosenOne;
	}
	
	public int maxValue(ArrayList<ArrayList<CandyNode>> boardState,int depth)
	{
		if(gameOver(boardState))
		{
			return utility(boardState);
		}
		if(depth <= 0)
		{
			return estimatedUtility(boardState);
		}
		count++;
		//System.out.println(count);
		
		int maxVal = Integer.MIN_VALUE;
		int nextDepth = depth-1;
		for(int i = 0;i < boardState.size();i++)
		{
			for(int j = 0;j < boardState.get(0).size();j++)
			{
				CandyNode action = boardState.get(i).get(j);
				if(action.getOwner() == '\0')
				{
					ArrayList<ArrayList<CandyNode>> nextBoardState = deepCloneBoard(boardState);
					nextBoardState.get(i).get(j).setOwner(maxPlayer);
					if(adjacentFriendlyCandy(nextBoardState,nextBoardState.get(i).get(j)))
					{
						infectEnemyCandy(nextBoardState,nextBoardState.get(i).get(j));
					}
					maxVal = Math.max(maxVal, minValue(nextBoardState,nextDepth));
				}
			}
		}
		return maxVal;
	}
	
	public int minValue(ArrayList<ArrayList<CandyNode>> boardState,int depth)
	{
		if(gameOver(boardState))
		{
			return utility(boardState);
		}
		if(depth <= 0)
		{
			return estimatedUtility(boardState);
		}
		
		count++;
		//System.out.println(count);
		
		int minVal = Integer.MAX_VALUE;
		int nextDepth = depth-1;
		for(int i = 0;i < boardState.size();i++)
		{
			for(int j = 0;j < boardState.get(0).size();j++)
			{
				CandyNode action = boardState.get(i).get(j);
				if(action.getOwner() == '\0')
				{
					ArrayList<ArrayList<CandyNode>> nextBoardState = deepCloneBoard(boardState);
					nextBoardState.get(i).get(j).setOwner(minPlayer);
					if(adjacentFriendlyCandy(nextBoardState,nextBoardState.get(i).get(j)))
					{
						infectEnemyCandy(nextBoardState,nextBoardState.get(i).get(j));
					}
					minVal = Math.min(minVal, maxValue(nextBoardState,nextDepth));
				}
			}
		}
		return minVal;
	}
	
	//NOTE: Utilities calculated from MAX's perspective
	public int utility(ArrayList<ArrayList<CandyNode>> boardState)
	{
		int utility = 0;
		for(int i = 0;i < boardState.size();i++)
		{
			for(int j = 0;j < boardState.get(0).size();j++)
			{
				if(boardState.get(i).get(j).getOwner() == maxPlayer)
				{
					utility += boardState.get(i).get(j).getValue();
				}
				if(boardState.get(i).get(j).getOwner() == minPlayer)
				{
					utility -= boardState.get(i).get(j).getValue();
				}
			}
		}
		return utility;
	}

	//NOTE: Utilities calculated from MAX's perspective
	public int estimatedUtility(ArrayList<ArrayList<CandyNode>> boardState)
	{
		int utility = 0;
		for(int i = 0;i < boardState.size();i++)
		{
			for(int j = 0;j < boardState.get(0).size();j++)
			{
				if(boardState.get(i).get(j).getOwner() == maxPlayer)
				{
					utility += boardState.get(i).get(j).getValue();
				}
				if(boardState.get(i).get(j).getOwner() == minPlayer)
				{
					utility -= boardState.get(i).get(j).getValue();
				}
			}
		}
		return utility;
	}
	
	public ArrayList<ArrayList<CandyNode>> deepCloneBoard(ArrayList<ArrayList<CandyNode>> board)
	{
		ArrayList<ArrayList<CandyNode>> cloneBoard = new ArrayList<ArrayList<CandyNode>>();
		for(int i = 0;i < 6;i++)
		{
			cloneBoard.add(new ArrayList<CandyNode>());
			for(int j = 0;j < 6;j++)
			{
				int value = board.get(i).get(j).getValue();
				char owner = board.get(i).get(j).getOwner();
				String displayValue = board.get(i).get(j).getDisplayValue();
				
				CandyNode newNode = new CandyNode(i,j,value);
				newNode.setOwner(owner);
				newNode.setDisplayValue(displayValue);
				cloneBoard.get(i).add(newNode);
				
			}
		}
		
		return cloneBoard;
	}
	
	public boolean gameOver(ArrayList<ArrayList<CandyNode>> boardState)
	{
		boolean gameOver = true;
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				if(boardState.get(i).get(j).getOwner() == '\0')
				{
					gameOver = false;
				}
			}
		}
		return gameOver;
	}
	
	public boolean adjacentFriendlyCandy(ArrayList<ArrayList<CandyNode>> board, CandyNode node)
	{	int row = node.getRow();
		int column = node.getColumn();
		
		//System.out.println(node.getOwner());
		boolean adjacentFriends = false;
		
		if(column - 1 >= 0)
		{
			adjacentFriends = ((board.get(row).get(column-1).getOwner() == node.getOwner()));
			if(adjacentFriends)
			{
				return adjacentFriends;
			}
		}
		if(column+1 < board.get(0).size())
		{
			adjacentFriends = ((board.get(row).get(column+1).getOwner() == node.getOwner()));
			if(adjacentFriends)
			{
				return adjacentFriends;
			}
		}
		if(row-1 >= 0)
		{
			adjacentFriends = ((board.get(row-1).get(column).getOwner() == node.getOwner()));
			if(adjacentFriends)
			{
				return adjacentFriends;
			}
		}
		if(row+1 < board.size())
		{
			adjacentFriends = ((board.get(row+1).get(column).getOwner() == node.getOwner()));
			if(adjacentFriends)
			{
				return adjacentFriends;
			}
		}
		
		return adjacentFriends;
	}
	
	public void infectEnemyCandy(ArrayList<ArrayList<CandyNode>> board, CandyNode node)
	{
		int row = node.getRow();
		int column = node.getColumn();
		
		System.out.println(node.getOwner());
		
		if(column - 1 >= 0 && board.get(row).get(column-1).getOwner() != node.getOwner() && board.get(row).get(column-1).getOwner() != '\0')
		{
			board.get(row).get(column-1).setOwner(node.getOwner());
		}
		if(column + 1 < board.get(0).size() && board.get(row).get(column+1).getOwner() != node.getOwner() && board.get(row).get(column+1).getOwner() != '\0')
		{
			board.get(row).get(column+1).setOwner(node.getOwner());
		}
		if(row-1 >= 0 && board.get(row-1).get(column).getOwner() != node.getOwner() && board.get(row-1).get(column).getOwner() != '\0')
		{
			board.get(row-1).get(column).setOwner(node.getOwner());
		}
		if(row + 1 < board.size() && board.get(row+1).get(column).getOwner() != node.getOwner() && board.get(row+1).get(column).getOwner() != '\0')
		{
			board.get(row+1).get(column).setOwner(node.getOwner());
		}
	}
}
