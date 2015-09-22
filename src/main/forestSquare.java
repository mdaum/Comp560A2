package main;

public class forestSquare {//this acts as the var in csp
int x,y;
char domain;
//perhaps map all associated constraints? not sure yet
public forestSquare(int x, int y, char domain){
	this.x=x;
	this.y=y;
	this.domain=domain; //E for empty, F for friend, T for tree
}
}
