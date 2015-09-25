package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HideSeekParser {
	int n;// num friends
	int t;// num trees, not really useful since we use hasNextLine but whatever
	ArrayList<forestSquare> squares = new ArrayList<forestSquare>(); //temporary holder until i figure out max x max y

	public HideSeekParser() {

	}

	public forestSquare[][] makeBoard(String filePath) {
		try {
			Scanner reader = new Scanner(new File(filePath));
			n = reader.nextInt();
			t = reader.nextInt();
			while (reader.hasNextLine()) {
				int row = reader.nextInt()-1;//going to start from 0 here....be sure to mention in readme
				int col = reader.nextInt()-1;//going to start from 0 here...be sure to mention in readme
				squares.add(new forestSquare(row,col,'T'));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		forestSquare[][] returnable=new forestSquare[n][n];
		for(int i=0;i<n;i++){ //initializing empty board
			for(int j=0;j<n;j++){
				returnable[i][j]=new forestSquare(i,j,'E');
			}
		}
		for (forestSquare square : squares) {//adding in trees
			returnable[square.row][square.col]=square;
		}
		
		return returnable;
	}

}
