package a3main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
public class Main 
{
	
	public static void main(String args[])
	{
		final String fileName = "Ayds.txt";
		
		ArrayList<ArrayList<CandyNode>> board = parseFile(fileName);
		printBoard(board);
	}
	
	public static ArrayList<ArrayList<CandyNode>> parseFile(String fileName)
	{
		Scanner in = null;;
		try 
		{
			in = new Scanner(new File(fileName));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}

		int height = 0; //current height of the board, 0-based index
		ArrayList<ArrayList<CandyNode>> board = new ArrayList<ArrayList<CandyNode>>();
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			board.add(new ArrayList<CandyNode>());
			Scanner lineScanner = new Scanner(line);
			int width = 0;
			
			while(lineScanner.hasNextInt())
			{
				board.get(height).add(new CandyNode(height,width,lineScanner.nextInt()));
				width++;
			}
			
			line = in.nextLine();
			height++;
		}
		
		return board;
	}
	
	public static void printBoard(ArrayList<ArrayList<CandyNode>> board)
	{
		for(int i = 0;i < board.size();i++)
		{
			for(int j = 0;j < board.get(0).size();j++)
			{
				System.out.print(board.get(i).get(j).getDisplayValue() + " ");
			}
			System.out.println();
		}
	}

}
