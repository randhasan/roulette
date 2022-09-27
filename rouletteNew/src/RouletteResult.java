// CS 0401
// RouletteResult class.  You must use this class as is in Assignment 2

public class RouletteResult
{
	private RColors color;	//Red, Black, Green, None
	private RRanges range;  //Low, High, None
	private RParities evo;  //Even, Odd, None
	private int value;
	
	public RouletteResult(RColors col, RRanges ran,
						  RParities eo, int val)
	{
		color = col;
		range = ran;
		evo = eo;
		value = val;
	}
	
	public String toString()
	{
		return new String("Value:" + value + "  Color:" + color + "  Range:" + range + "  Parity:" + evo);
	}
}
