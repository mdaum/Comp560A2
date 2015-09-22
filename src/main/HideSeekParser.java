package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HideSeekParser {
	int n;// num friends
	int t;// num trees, not really useful since we use hasNextLine but whatever
	int Xmax = -1;//using for array construction i.e board returned in makeBoard
	int Ymax = -1;//using for array construction
	ArrayList<forestSquare> squares = new ArrayList<forestSquare>(); //temporary holder until i figure out max x max y

	public HideSeekParser() {

	}

	public forestSquare[][] makeBoard(String filePath) {
		try {
			Scanner reader = new Scanner(new File(filePath));
			n = reader.nextInt();
			t = reader.nextInt();
			while (reader.hasNextLine()) {
				int x = reader.nextInt()-1;//going to start from 0 here....be sure to mention in readme
				if(x>Xmax)Xmax=x;
				int y = reader.nextInt()-1;//going to start from 0 here...be sure to mention in readme
				if(y>Ymax)Ymax=y;
				squares.add(new forestSquare(x,y,'T'));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		forestSquare[][] returnable=new forestSquare[Xmax+1][Ymax+1];
		for(int i=0;i<Xmax+1;i++){ //initializing empty board
			for(int j=0;j<Ymax+1;j++){
				returnable[i][j]=new forestSquare(i,j,'E');
			}
		}
		for (forestSquare square : squares) {//adding in trees
			returnable[square.x][square.y]=square;
		}
		
		return returnable;
	}

}
