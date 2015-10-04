package a3main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
public class Main 
{
	
	public static void main(String args[])
	{
		final String fileName = "Mounds.txt";
		Scanner in = new Scanner(System.in);
		ArrayList<ArrayList<CandyNode>> board = parseFile(fileName);
		printBoard(board);
		Player blue = new Player('B');
		Player green = new Player('G');
		MinimaxSearch victory = new MinimaxSearch(blue,green,board);
		victory.depthLimitedSearch(2);
		printBoard(board);
		System.out.println("Blue's score is: " +blue.getTotalScore());
		System.out.println("Green's score is: " + green.getTotalScore());
		System.out.println("Number of nodes expanded by Blue is " + blue.numNodesExpanded);
		System.out.println("Number of nodes expanded by Green is " + green.numNodesExpanded);
		/*while(!victory.gameOver(board))
		{
		in.next();
		victory.playOneMove(2);
		printBoard(board);
		System.out.println(board.get(5).get(5).getOwner());
		}*/
	}
	
	public static ArrayList<ArrayList<CandyNode>> parseFile(String fileName)
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
		
		for(int i = 0;i < 6;i++)
		{
			board.add(new ArrayList<CandyNode>());
			for(int j = 0;j < 6;j++)
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
					System.out.print(board.get(i).get(j).getDisplayValue() + " ");
				}
				else
				{
					System.out.print(board.get(i).get(j).getDisplayValue() + "  ");
				}
			}
			System.out.println();
		}
	}

}
