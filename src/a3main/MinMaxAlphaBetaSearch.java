package a3main;

import java.util.ArrayList;

public class MinMaxAlphaBetaSearch 
{
	public int numNodesExpanded = 0;
	public int count = 0;
	private Player blue,green;
	public ArrayList<ArrayList<CandyNode>> board;
	private boolean blueTurn;
	
	public char maxPlayer;
	public char minPlayer;
	public int numMoves = 0;
	public long timeExpired = 0;
	
	public MinMaxAlphaBetaSearch(Player blue, Player green, ArrayList<ArrayList<CandyNode>> board)
	{
		this.blue = blue;
		this.setGreen(green);
		this.board = board;
		this.blueTurn = true;
		this.numNodesExpanded = 0;
		this.maxPlayer = blue.getName();
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
				minPlayer = getGreen().getName();
			}
			else
			{
				maxPlayer = getGreen().getName();
				minPlayer = blue.getName();
			}
			long beginningTime = System.currentTimeMillis();
			CandyNode decision = minimaxDecision(boardState,depth);
			long endTime = System.currentTimeMillis();
			timeExpired += endTime - beginningTime;
			
			numMoves++;
			System.out.println(maxPlayer + ": drop at " + (char)((Integer.toString(decision.getColumn()).charAt(0)) + 17) + "" + decision.getRow());
			
			board.get(decision.getRow()).get(decision.getColumn()).setOwner(maxPlayer);
			if(adjacentFriendlyCandy(board,board.get(decision.getRow()).get(decision.getColumn())))
			{
				//System.out.println("Infecting...");
				infectEnemyCandy(board,board.get(decision.getRow()).get(decision.getColumn()));
			}
			blueTurn = !blueTurn;
		}
		blue.setTotalScore(calculateScore(blue.getName()));
		getGreen().setTotalScore(calculateScore(getGreen().getName()));
		return null;
	}
	
	public void playOneMoveAgainstOtherAgent(int depth)
	{
		ArrayList<ArrayList<CandyNode>> boardState = deepCloneBoard(board);
		CandyNode decision = minimaxDecision(boardState,depth);
		System.out.println(maxPlayer + ": drop at " + (char)((Integer.toString(decision.getColumn()).charAt(0)) + 17) + "" + decision.getRow());
		//System.out.println("Decision to make move at " + decision.getColumn() + " , " + decision.getRow());
		if(decision != null)
		{
			board.get(decision.getRow()).get(decision.getColumn()).setOwner(maxPlayer);
			if(adjacentFriendlyCandy(board,board.get(decision.getRow()).get(decision.getColumn())))
			{
				//System.out.println("Infecting...");
				infectEnemyCandy(board,board.get(decision.getRow()).get(decision.getColumn()));
			}
		}
	}
	
	public void makeMove(ArrayList<ArrayList<CandyNode>> board,CandyNode decision)
	{
		char tmp = maxPlayer;
		maxPlayer = green.getName();
		board.get(decision.getRow()).get(decision.getColumn()).setOwner(maxPlayer);
		if(adjacentFriendlyCandy(board,board.get(decision.getRow()).get(decision.getColumn())))
		{
			System.out.println("Infecting...");
			infectEnemyCandy(board,board.get(decision.getRow()).get(decision.getColumn()));
		}
		maxPlayer = tmp;
	}
	
	public void playOneMove(int depth)
	{
		System.out.println("Entering search...");
		ArrayList<ArrayList<CandyNode>> boardState = deepCloneBoard(board);
		if(blueTurn)
		{
			maxPlayer = blue.getName();
			minPlayer = getGreen().getName();
		}
		else
		{
			maxPlayer = getGreen().getName();
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
		int alpha=Integer.MIN_VALUE;
		int beta=Integer.MAX_VALUE;
		//System.out.println(maxPlayer);
		CandyNode chosenOne = null;

		int maxVal = Integer.MIN_VALUE;
		for(int i = 0;i < board.size();i++)
		{
			for(int j = 0;j < board.get(0).size();j++)
			{
				CandyNode action = boardState.get(i).get(j);
				if(maxPlayer == blue.getName())
				{
					blue.numNodesExpanded++;
				}
				else
				{
					getGreen().numNodesExpanded++;
				}
				if(action.getOwner() == '\0')
				{
					ArrayList<ArrayList<CandyNode>> nextBoardState = deepCloneBoard(boardState);
					nextBoardState.get(i).get(j).setOwner(maxPlayer);
					if(adjacentFriendlyCandy(nextBoardState,nextBoardState.get(i).get(j)))
					{
						infectEnemyCandy(nextBoardState,nextBoardState.get(i).get(j));
					}
					
					int minVal = minValue(nextBoardState,depth,alpha,beta);
					
					if(maxVal < minVal)
					{
						maxVal = minVal;
						chosenOne = boardState.get(i).get(j);
					}
				}
				alpha=maxVal;
			}
		}
		return chosenOne;
	}
	
	public int maxValue(ArrayList<ArrayList<CandyNode>> boardState,int depth, int alpha, int beta)
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
				if(maxPlayer == blue.getName())
				{
					blue.numNodesExpanded++;
				}
				else
				{
					getGreen().numNodesExpanded++;
				}
				if(action.getOwner() == '\0')
				{
					ArrayList<ArrayList<CandyNode>> nextBoardState = deepCloneBoard(boardState);
					nextBoardState.get(i).get(j).setOwner(maxPlayer);
					if(adjacentFriendlyCandy(nextBoardState,nextBoardState.get(i).get(j)))
					{
						infectEnemyCandy(nextBoardState,nextBoardState.get(i).get(j));
					}
					maxVal = Math.max(maxVal, minValue(nextBoardState,nextDepth,alpha,beta));
					if(maxVal>=beta)return maxVal;
					alpha=Math.max(alpha, maxVal);
				}
			}
		}
		return maxVal;
	}
	
	public int minValue(ArrayList<ArrayList<CandyNode>> boardState,int depth, int alpha, int beta)
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
				if(maxPlayer == blue.getName())
				{
					blue.numNodesExpanded++;
				}
				else
				{
					getGreen().numNodesExpanded++;
				}
				if(action.getOwner() == '\0')
				{
					ArrayList<ArrayList<CandyNode>> nextBoardState = deepCloneBoard(boardState);
					nextBoardState.get(i).get(j).setOwner(minPlayer);
					if(adjacentFriendlyCandy(nextBoardState,nextBoardState.get(i).get(j)))
					{
						infectEnemyCandy(nextBoardState,nextBoardState.get(i).get(j));
					}
					minVal = Math.min(minVal, maxValue(nextBoardState,nextDepth,alpha,beta));
					if(minVal<=alpha)return minVal;
					beta=Math.min(beta, minVal);
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
	public int estimatedUtility(ArrayList<ArrayList<CandyNode>> boardState) // SAFE
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
				//if(boardState.get(i).get(j).getOwner()=='\0') utility+=infectionValue(boardState,boardState.get(i).get(j));
			}
		}
		return utility;
	}

	
	public ArrayList<ArrayList<CandyNode>> deepCloneBoard(ArrayList<ArrayList<CandyNode>> board)
	{
		ArrayList<ArrayList<CandyNode>> cloneBoard = new ArrayList<ArrayList<CandyNode>>();
		for(int i = 0;i < board.size();i++)
		{
			cloneBoard.add(new ArrayList<CandyNode>());
			for(int j = 0;j < board.get(0).size();j++)
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
	public boolean adjacentEnemyCandy(ArrayList<ArrayList<CandyNode>> board, CandyNode node){
		int row = node.getRow();
		int column = node.getColumn();
		
		//System.out.println(node.getOwner());
		boolean adjacentEnemy = false;
		
		if(column - 1 >= 0)
		{
			adjacentEnemy = ((board.get(row).get(column-1).getOwner() != node.getOwner())&&board.get(row).get(column-1).getOwner()!='\0');
			if(adjacentEnemy)
			{
				return adjacentEnemy;
			}
		}
		if(column+1 < board.get(0).size())
		{
			adjacentEnemy = ((board.get(row).get(column+1).getOwner() != node.getOwner())&&board.get(row).get(column+1).getOwner()!='\0');
			if(adjacentEnemy)
			{
				return adjacentEnemy;
			}
		}
		if(row-1 >= 0)
		{
			adjacentEnemy = ((board.get(row-1).get(column).getOwner() != node.getOwner())&&board.get(row-1).get(column).getOwner()!='\0');
			if(adjacentEnemy)
			{
				return adjacentEnemy;
			}
		}
		if(row+1 < board.size())
		{
			adjacentEnemy = ((board.get(row+1).get(column).getOwner() != node.getOwner())&&board.get(row+1).get(column).getOwner()!='\0');
			if(adjacentEnemy)
			{
				return adjacentEnemy;
			}
		}
		
		return adjacentEnemy;
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
		
		//System.out.println(node.getOwner());
		
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
	
	public boolean isInfectionArea(ArrayList<ArrayList<CandyNode>> board, CandyNode node){
		int row = node.getRow();
		int col = node.getColumn();
		boolean hasAdjacentMaxCandy=false,hasAdjacentMinCandy=false;
		if(row>0){
			if(board.get(row-1).get(col).getOwner()==maxPlayer)
				hasAdjacentMaxCandy=true;
			else if(board.get(row-1).get(col).getOwner()==minPlayer)
				hasAdjacentMinCandy=true;
		}
		if(row<board.size()-1){
			if(board.get(row+1).get(col).getOwner()==maxPlayer)
				hasAdjacentMaxCandy=true;
			else if(board.get(row+1).get(col).getOwner()==minPlayer)
				hasAdjacentMinCandy=true;
		}
		if(col>0){
			if(board.get(row).get(col-1).getOwner()==maxPlayer)
				hasAdjacentMaxCandy=true;
			else if(board.get(row).get(col-1).getOwner()==minPlayer)
				hasAdjacentMinCandy=true;
		}
		if(col<board.size()-1){
			if(board.get(row).get(col+1).getOwner()==maxPlayer)
				hasAdjacentMaxCandy=true;
			else if(board.get(row).get(col+1).getOwner()==minPlayer)
				hasAdjacentMinCandy=true;
		}
		return hasAdjacentMaxCandy && hasAdjacentMinCandy;
	}
	
	public int infectionValue(ArrayList<ArrayList<CandyNode>> boardState, CandyNode node){
		int curr=0;
		int row=node.getRow();
		int column = node.getColumn();
		if(isInfectionArea(boardState,node)){
			if(column - 1 >= 0 && board.get(row).get(column-1).getOwner() != '\0')
			{
				if(board.get(row).get(column-1).getOwner()==maxPlayer)curr-=board.get(row).get(column-1).getValue();
				if(board.get(row).get(column-1).getOwner()==minPlayer)curr+=board.get(row).get(column-1).getValue();
				//else curr+=board.get(row).get(column-1).getValue();
			}
			if(column + 1 < board.get(0).size() && board.get(row).get(column+1).getOwner() != '\0')
			{
				if(board.get(row).get(column+1).getOwner()==maxPlayer)curr-=board.get(row).get(column+1).getValue();
				if(board.get(row).get(column+1).getOwner()==minPlayer)curr+=board.get(row).get(column+1).getValue();
				//else curr+=board.get(row).get(column+1).getValue();
			}
			if(row-1 >= 0 &&  board.get(row-1).get(column).getOwner() != '\0')
			{
				if(board.get(row-1).get(column).getOwner()==maxPlayer)curr-=board.get(row-1).get(column).getValue();
				if(board.get(row-1).get(column).getOwner()==minPlayer)curr+=board.get(row-1).get(column).getValue();
				//else curr+=board.get(row-1).get(column).getValue();
			}
			if(row + 1 < board.size() && board.get(row+1).get(column).getOwner() != '\0')
			{
				if(board.get(row+1).get(column).getOwner()==maxPlayer)curr-=board.get(row+1).get(column).getValue();
				if(board.get(row+1).get(column).getOwner()==minPlayer)curr+=board.get(row+1).get(column).getValue();
				//else curr+=board.get(row+1).get(column).getValue();
			}
		}
		return curr;
	}
	
	public int calculateScore(char name)
	{
		int sum = 0;
		for(int i = 0;i < board.size();i++)
		{
			for(int j = 0;j < board.get(0).size();j++)
			{
				if(board.get(i).get(j).getOwner() == name)
				{
					sum += board.get(i).get(j).getValue();
				}
			}
		}
		return sum;
	}

	public Player getGreen() {
		return green;
	}

	public void setGreen(Player green) {
		this.green = green;
	}
	
	public Player getBlue()
	{
		return blue;
	}
}
