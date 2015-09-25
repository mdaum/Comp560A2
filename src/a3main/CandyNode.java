package a3main;

public class CandyNode 
{
	private char owner;
	private String displayValue;
	private int row,column;
	private int costOfBestPathHere;
	private int value;
	
	public CandyNode(int row, int column)
	{
		this.row = row;
		this.column = column;
		
		owner = '\0';
		value = (int)((Math.random()*98)+1); 
		displayValue = ((Integer)value).toString();
		costOfBestPathHere = 0;
	}
	
	public CandyNode(int row, int column, int value)
	{
		this(row,column);
		this.value = value;
		displayValue = ((Integer)(this.value)).toString();
	}
	
	public void incrementCostOfBestPathHere(int amount)
	{
		costOfBestPathHere += amount;
	}
	public char getOwner()
	{
		return owner;
	}
	
	public void setOwner(char owner)
	{
		this.owner = owner;
	}
	
	public String getDisplayValue()
	{
		return displayValue;
	}
	
	public void setDisplayValue(String displayValue)
	{
		this.displayValue = displayValue;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public void setRow(int row)
	{
		this.row = row;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public void setColumn(int column)
	{
		this.column = column;
	}
	
	public int getCostOfBestPathHere()
	{
		return costOfBestPathHere;
	}
	
	public void setCostOfBestPathHere(int costOfBestPathHere)
	{
		this.costOfBestPathHere = costOfBestPathHere;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
}
