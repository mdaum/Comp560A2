package a3main;

public class Player 
{
	private int totalScore;
	private char name;
	
	public Player(char name)
	{
		this.name = name;
		totalScore = 0;
	}
	
	public void incrementScore(int amount)
	{
		totalScore += amount;
	}
	public int getTotalScore()
	{
		return totalScore;
	}
	
	public void setTotalScore(int totalScore)
	{
		this.totalScore = totalScore;
	}
	
	public char getName()
	{
		return name;
	}
	
	public void setName(char name)
	{
		this.name = name;
	}
}
