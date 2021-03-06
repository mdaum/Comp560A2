package a3main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
public class Main 
{
	
	public static void main(String args[])
	{
		int boardSize = 6;
		int minimaxDepth = 2; //really this +1
		int alphabetaDepth = 4; //really this +1
		String fileName = "ReesesPieces.txt";
		if(args.length > 0)
		{
			minimaxDepth = Integer.parseInt(args[0])-1;
		}
		
		if(args.length > 1)
		{
			alphabetaDepth = Integer.parseInt(args[1])-1;
		}
		
		if(args.length > 2)
		{
			fileName = args[2];
		}
		
		if(args.length <= 3)
		{
		
		Scanner in = new Scanner(System.in);
		ArrayList<ArrayList<CandyNode>> board = parseFile(fileName,boardSize);
		System.out.println("Regular Minimax Search at depth "+(minimaxDepth+1)+":");
		printBoard(board);
		Player blue = new Player('B');
		Player green = new Player('G');
		Player trueblue = new Player('B');
		Player truegreen = new Player('G');
		MinimaxSearch victory = new MinimaxSearch(blue,green,board);
		ArrayList<ArrayList<CandyNode>> trueboard = victory.deepCloneBoard(board);
		//we need copies of the board for minimax vs alpha-beta and vice versa
		ArrayList<ArrayList<CandyNode>> mmabBoard = victory.deepCloneBoard(board);
		ArrayList<ArrayList<CandyNode>> abmmBoard = victory.deepCloneBoard(board);
		MinMaxAlphaBetaSearch trueVictory = new MinMaxAlphaBetaSearch(trueblue,truegreen,trueboard);
		victory.depthLimitedSearch(minimaxDepth);
		printBoard(board);
		System.out.println("Blue's score is: " +blue.getTotalScore());
		System.out.println("Green's score is: " + green.getTotalScore());
		System.out.println("Number of nodes expanded by Blue is " + blue.numNodesExpanded);
		System.out.println("Number of nodes expanded by Green is " + green.numNodesExpanded);
		System.out.println("Average number of nodes expanded per move is " + ((double)(blue.numNodesExpanded + green.numNodesExpanded))/(double)(victory.numMoves));
		System.out.println("Average number of time taken per move is " + (double)(victory.timeExpired)/(double)(victory.numMoves) + " milliseconds");
		System.out.println("Alpha Beta at depth "+(alphabetaDepth+1)+":");
		printBoard(trueboard);
		trueVictory.depthLimitedSearch(alphabetaDepth);
		printBoard(trueboard);
		System.out.println("Blue's score is: " +trueblue.getTotalScore());
		System.out.println("Green's score is: " + truegreen.getTotalScore());
		System.out.println("Number of nodes expanded by Blue is " + trueblue.numNodesExpanded);
		System.out.println("Number of nodes expanded by Green is " + truegreen.numNodesExpanded);
		System.out.println("Average number of nodes expanded per move is " + ((double)(trueblue.numNodesExpanded + truegreen.numNodesExpanded))/(double)(trueVictory.numMoves));
		System.out.println("Average number of time taken per move is " + (double)(trueVictory.timeExpired)/(double)(trueVictory.numMoves) + " milliseconds");
		//MINIMAX VS. ALPHA-BETA//////////////////////////////////////////////////////////////////////////////
		System.out.println("Minimax(blue) at depth "+(minimaxDepth+1)+" versus Alpha Beta(green) at depth "+(alphabetaDepth+1));
		printBoard(mmabBoard);
		System.out.println("Entering search...");
		Player blueMMFirst = new Player('B');
		Player greenABSecond = new Player('G');
		MinimaxSearch mmFirst = new MinimaxSearch(blueMMFirst,greenABSecond,mmabBoard);
		mmFirst.maxPlayer='B'; mmFirst.minPlayer='G';
		MinMaxAlphaBetaSearch abSecond = new MinMaxAlphaBetaSearch(blueMMFirst,greenABSecond,mmabBoard);
		abSecond.maxPlayer='G'; abSecond.minPlayer='B';
		long greenABTime=0; long blueMMTime=0;
		while(!mmFirst.gameOver(mmabBoard)){//the game should be over when the first agent hits a dead end (there will always be an even number of moves)
			long startTime=System.currentTimeMillis();
			mmFirst.playOneMoveAgainstOtherAgent(minimaxDepth); //minimax plays as Blue
			blueMMTime+=System.currentTimeMillis()-startTime;
			//System.out.println(mmFirst.maxPlayer+" should be "+mmFirst.getBlue().getName());
			startTime=System.currentTimeMillis();
			abSecond.playOneMoveAgainstOtherAgent(alphabetaDepth); //alphabeta plays as Green
			greenABTime+=System.currentTimeMillis()-startTime;
			//System.out.println(abSecond.maxPlayer+" should be "+abSecond.getGreen().getName());
			//printBoard(mmabBoard);
		}
		printBoard(mmabBoard);
		blueMMFirst.setTotalScore(mmFirst.calculateScore('B'));
		greenABSecond.setTotalScore(abSecond.calculateScore('G'));
		System.out.println("Blue's score is: " + blueMMFirst.getTotalScore());
		System.out.println("Green's score is: " + greenABSecond.getTotalScore());
		System.out.println("Number of nodes expanded by Blue is " + blueMMFirst.numNodesExpanded);
		System.out.println("Number of nodes expanded by Green is " + greenABSecond.numNodesExpanded);
		System.out.println("Average number of nodes expanded per move is " + ((double)(blueMMFirst.numNodesExpanded + greenABSecond.numNodesExpanded))/(double)(36));
		System.out.println("Average number of nodes expanded by Blue per move is " + (double)(blueMMFirst.numNodesExpanded)/(double)(16));
		System.out.println("Average number of nodes expanded by Green per move is " + (double)(greenABSecond.numNodesExpanded)/(double)(16));
		System.out.println("Average time per turn by Blue is "+(blueMMTime/(long)16)+" milliseconds");
		System.out.println("Average time per turn by Green is "+(greenABTime/(long)16)+" milliseconds");
		//ALPHA-BETA VS. MINIMAX//////////////////////////////////////////////////////////////////////////////
		System.out.println("Alpha Beta(blue) at depth "+(alphabetaDepth+1)+" versus Minimax(green) at depth "+(minimaxDepth+1));
		printBoard(abmmBoard);
		System.out.println("Entering search...");
		Player blueABFirst = new Player('B');
		Player greenMMSecond = new Player('G');
		MinimaxSearch mmSecond = new MinimaxSearch(blueABFirst,greenMMSecond,abmmBoard);
		mmSecond.maxPlayer='G'; mmSecond.minPlayer='B';
		MinMaxAlphaBetaSearch abFirst = new MinMaxAlphaBetaSearch(blueABFirst,greenMMSecond,abmmBoard);
		abFirst.maxPlayer='B'; abFirst.minPlayer='G';
		long blueABTime=0; long greenMMTime=0;
		while(!abFirst.gameOver(abmmBoard)){//the game should be over when the first agent hits a dead end (there will always be an even number of moves)
			long startTime=System.currentTimeMillis();
			abFirst.playOneMoveAgainstOtherAgent(alphabetaDepth); //minimax plays as Blue
			blueABTime+=System.currentTimeMillis()-startTime;
			//System.out.println(mmFirst.maxPlayer+" should be "+mmFirst.getBlue().getName());
			startTime=System.currentTimeMillis();
			mmSecond.playOneMoveAgainstOtherAgent(minimaxDepth); //alphabeta plays as Green
			greenMMTime+=System.currentTimeMillis()-startTime;
			//System.out.println(abSecond.maxPlayer+" should be "+abSecond.getGreen().getName());
			//printBoard(mmabBoard);
		}
		printBoard(abmmBoard);
		blueABFirst.setTotalScore(abFirst.calculateScore('B'));
		greenMMSecond.setTotalScore(mmSecond.calculateScore('G'));
		System.out.println("Blue's score is: " + blueABFirst.getTotalScore());
		System.out.println("Green's score is: " + greenMMSecond.getTotalScore());
		System.out.println("Number of nodes expanded by Blue is " + blueABFirst.numNodesExpanded);
		System.out.println("Number of nodes expanded by Green is " + greenMMSecond.numNodesExpanded);
		System.out.println("Average number of nodes expanded per move is " + ((double)(blueABFirst.numNodesExpanded + greenMMSecond.numNodesExpanded))/(double)(36));
		System.out.println("Average number of nodes expanded by Blue per move is " + (double)(blueABFirst.numNodesExpanded)/(double)(16));
		System.out.println("Average number of nodes expanded by Green per move is " + (double)(greenMMSecond.numNodesExpanded)/(double)(16));
		System.out.println("Average time per turn by Blue is "+(blueABTime)/(long)16+" milliseconds");
		System.out.println("Average time per turn by Green is "+(greenMMTime)/(long)16+" milliseconds");
		/////////////////////////////////////////////////////////////////////////////////////////
		/*while(!victory.gameOver(board))
		{
		in.next();
		victory.playOneMove(2);
		printBoard(board);
		System.out.println(board.get(5).get(5).getOwner());
		}*/

		/*while(!victory.gameOver(board))
		{
		in.next();
		victory.playOneMove(2);
		printBoard(board);
		System.out.println(board.get(5).get(5).getOwner());
		}*/
		/*while(!victory.gameOver(board))
		{
		in.next();
		victory.playOneMove(2);
		printBoard(board);
		System.out.println(board.get(5).get(5).getOwner());
		}*/
		
		}
		if(args.length == 4)
		{
			boardSize = Integer.parseInt(args[3]);
			ArrayList<ArrayList<CandyNode>> board = makeBoardOfSize(boardSize);
			System.out.println("Regular Minimax Search at depth "+(minimaxDepth+1)+":");
			printBoard(board);
			Player blue = new Player('B');
			Player green = new Player('G');
			Player trueblue = new Player('B');
			Player truegreen = new Player('G');
			MinimaxSearch victory = new MinimaxSearch(blue,green,board);
			ArrayList<ArrayList<CandyNode>> trueboard = victory.deepCloneBoard(board);
			//we need copies of the board for minimax vs alpha-beta and vice versa
			ArrayList<ArrayList<CandyNode>> mmabBoard = victory.deepCloneBoard(board);
			ArrayList<ArrayList<CandyNode>> abmmBoard = victory.deepCloneBoard(board);
			MinMaxAlphaBetaSearch trueVictory = new MinMaxAlphaBetaSearch(trueblue,truegreen,trueboard);
			victory.depthLimitedSearch(minimaxDepth);
			printBoard(board);
			System.out.println("Blue's score is: " +blue.getTotalScore());
			System.out.println("Green's score is: " + green.getTotalScore());
			System.out.println("Number of nodes expanded by Blue is " + blue.numNodesExpanded);
			System.out.println("Number of nodes expanded by Green is " + green.numNodesExpanded);
			System.out.println("Average number of nodes expanded per move is " + ((double)(blue.numNodesExpanded + green.numNodesExpanded))/(double)(victory.numMoves));
			System.out.println("Average number of time taken per move is " + (double)(victory.timeExpired)/(double)(victory.numMoves) + " milliseconds");
			System.out.println("Alpha Beta at depth "+(alphabetaDepth+1)+":");
			printBoard(trueboard);
			trueVictory.depthLimitedSearch(alphabetaDepth);
			printBoard(trueboard);
			System.out.println("Blue's score is: " +trueblue.getTotalScore());
			System.out.println("Green's score is: " + truegreen.getTotalScore());
			System.out.println("Number of nodes expanded by Blue is " + trueblue.numNodesExpanded);
			System.out.println("Number of nodes expanded by Green is " + truegreen.numNodesExpanded);
			System.out.println("Average number of nodes expanded per move is " + ((double)(trueblue.numNodesExpanded + truegreen.numNodesExpanded))/(double)(trueVictory.numMoves));
			System.out.println("Average number of time taken per move is " + (double)(trueVictory.timeExpired)/(double)(trueVictory.numMoves) + " milliseconds");
		}
		if(args.length == 5)
		{
			if(args[4].equals("M"))
			{
			System.out.println("So you think you are good enough to battle our Computer Overlords?");
			System.out.println("Enter each move using [column] [row] with one space between column and row, where column and row are integers");
			System.out.println("You are the green player, your squares will be denoted by a G");
			Scanner in = new Scanner(System.in);
			ArrayList<ArrayList<CandyNode>> board = parseFile(fileName,boardSize);
			printBoard(board);
			Player blue = new Player('B');
			Player green = new Player('G');
			MinimaxSearch victory = new MinimaxSearch(blue,green,board);
			ArrayList<ArrayList<CandyNode>> origBoard = victory.deepCloneBoard(board);
			while(!victory.gameOver(board))
			{
				System.out.println("Enter any character to let the computer make its move");
				in.next();
				victory.playOneMoveAgainstOtherAgent(minimaxDepth);
				printBoard(board);
				if(!victory.gameOver(board))
				{
					System.out.println("For your convenience, here is the original board and point values:");
					printBoard(origBoard);
					System.out.println("Enter your move:");
					int column = in.nextInt();
					int row = in.nextInt();
					victory.makeMove(board, board.get(row).get(column));
					printBoard(board);
				}
			}
			if(victory.calculateScore(victory.getBlue().getName()) > victory.calculateScore(victory.getGreen().getName()))
			{
				System.out.println("You lost!");
				System.out.println("Final Score was:");
				System.out.println("Blue: " + victory.calculateScore(victory.getBlue().getName()));
				System.out.println("You: " + victory.calculateScore(victory.getGreen().getName()));
			}
			else
			{
				System.out.println("You won! (You win ties as well!)");
				System.out.println("Final Score was:");
				System.out.println("Blue: " + victory.calculateScore(victory.getBlue().getName()));
				System.out.println("You: " + victory.calculateScore(victory.getGreen().getName()));
			}
			}
			else if(args[4].equals("A"))
			{
				System.out.println("So you think you are good enough to battle our Computer Overlords?");
				System.out.println("Enter each move using [column] [row] with one space between column and row, where column and row are integers");
				System.out.println("You are the green player, your squares will be denoted by a G");
				Scanner in = new Scanner(System.in);
				ArrayList<ArrayList<CandyNode>> board = parseFile(fileName,boardSize);
				printBoard(board);
				Player blue = new Player('B');
				Player green = new Player('G');
				MinMaxAlphaBetaSearch victory = new MinMaxAlphaBetaSearch(blue,green,board);
				ArrayList<ArrayList<CandyNode>> origBoard = victory.deepCloneBoard(board);
				while(!victory.gameOver(board))
				{
					System.out.println("Enter any character to let the computer make its move");
					in.next();
					victory.playOneMoveAgainstOtherAgent(minimaxDepth);
					printBoard(board);
					if(!victory.gameOver(board))
					{
						System.out.println("For your convenience, here is the original board and point values:");
						printBoard(origBoard);
						System.out.println("Enter your move:");
						int column = in.nextInt();
						int row = in.nextInt();
						victory.makeMove(board, board.get(row).get(column));
						printBoard(board);
					}
				}
				if(victory.calculateScore(victory.getBlue().getName()) > victory.calculateScore(victory.getGreen().getName()))
				{
					System.out.println("You lost!");
					System.out.println("Final Score was:");
					System.out.println("Blue: " + victory.calculateScore(victory.getBlue().getName()));
					System.out.println("You: " + victory.calculateScore(victory.getGreen().getName()));
				}
				else
				{
					System.out.println("You won! (You win ties as well!)");
					System.out.println("Final Score was:");
					System.out.println("Blue: " + victory.calculateScore(victory.getBlue().getName()));
					System.out.println("You: " + victory.calculateScore(victory.getGreen().getName()));
				}
			}
		}
	}
	
	public static ArrayList<ArrayList<CandyNode>> parseFile(String fileName, int size)
	{
		Scanner in = null;
		try 
		{
			in = new Scanner(new File(fileName));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		ArrayList<ArrayList<CandyNode>> board = new ArrayList<ArrayList<CandyNode>>();
		
		for(int i = 0;i < size;i++)
		{
			board.add(new ArrayList<CandyNode>());
			for(int j = 0;j < size;j++)
			{
				int value = in.nextInt();
				board.get(i).add(new CandyNode(i,j,value));
			}
		}
		
		return board;
	}
	
	public static void printBoard(ArrayList<ArrayList<CandyNode>> board)
	{
		for(int i = 0;i < board.size();i++)
		{
			for(int j = 0;j < board.get(0).size();j++)
			{
				if(board.get(i).get(j).getDisplayValue().length() == 2)
				{
					System.out.print(board.get(i).get(j).getDisplayValue());
				}
				else
				{
					System.out.print(board.get(i).get(j).getDisplayValue() + " ");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public static ArrayList<ArrayList<CandyNode>> makeBoardOfSize(int size)
	{
		ArrayList<ArrayList<CandyNode>> board = new ArrayList<ArrayList<CandyNode>>();
		for(int i = 0;i < size;i++)
		{
			board.add(new ArrayList<CandyNode>());
			for(int j = 0;j < size;j++)
			{
				board.get(i).add(new CandyNode(i,j));
			}
		}
		return board;
	}

}
