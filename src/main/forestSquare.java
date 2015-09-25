package main;

public class forestSquare {//this acts as the var in csp
int row,col;
char value;
//perhaps map all associated constraints? not sure yet
public forestSquare(int row, int col, char domain){
	this.row=row;
	this.col=col;
	this.value=domain; //E for empty, F for friend, T for tree
}
}
