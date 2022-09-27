import java.util.Random;

/**
 * @author Rand Hasan, Period 11
 * 1/28/2019
 * RouletteWheel.java CHS CS 401 Assignment 2
 * This program represents the Roulette wheel and sets up the wheel data and finds the payoff for a  user's bet.
*/

public class RouletteWheel {
	private int v; // value of the part of the wheel the balls onto after the wheel is spun
	private String c; // color of the part of the wheel the balls onto after the wheel is spun
	private String r; // range (whether the value is high or low) of the part of the wheel the balls onto after the wheel is spun
	private String p; // parity (whether the value is odd or even) of the part of the wheel the balls onto after the wheel is spun
	
	/**
	 * Spins the wheel by selecting a random number (0-36), stores the result of the spin, and returns a RouletteResult object with the appropriate fields.
	 * @return result a RouletteResult object with the fields based of the random number that was selected
	 */
	public RouletteResult spinWheel() {
		Random rand = new Random();
		int spinNum = rand.nextInt(37);
		RColors color = RColors.None; // initialize values
		RRanges ranges = RRanges.None;
		RParities parities = RParities.None;
		
		if (spinNum == 0) // player will lose if it lands on 0
		{
			c = "Green";
			r = "None";
			p = "None";
			color = RColors.Green;
		}
		else if ((spinNum > 0 && spinNum < 11) || (spinNum > 18 && spinNum < 29))
		{
			if (spinNum%2 == 0)
			{
				c = "Black";
				p = "Even";
				color = RColors.Black;
				parities = RParities.Even;
			}
			else
			{
				c = "Red";
				p = "Odd";
				color = RColors.Red;
				parities = RParities.Odd;
			}
		}
		else if ((spinNum > 10 && spinNum < 19) || (spinNum > 28 && spinNum < 37))
		{
			if (spinNum%2 == 0)
			{
				c = "Red";
				p = "Even";
				color = RColors.Red;
				parities = RParities.Even;
			}
			else
			{
				c = "Black";
				p = "Odd";
				color = RColors.Black;
				parities = RParities.Odd;
			}
		}
		if (spinNum > 0 && spinNum < 19)
		{
			ranges = RRanges.Low;
			r = "Low";
		}
		else if (spinNum > 18 && spinNum < 37)
		{
			ranges = RRanges.High;
			r = "High";
		}
		v = spinNum;
		RouletteResult result = new RouletteResult(color, ranges, parities, spinNum);
		return result;
	}
	
	/**
	 * Checks the current value of the wheel versus the data in the RouletteBet argument
	 * @param b the RouletteBet argument that the currect value of the wheel is compared to
	 * @return the payoff based on the comparision of the value of the wheel and the data in the RouletteBet argument
	 */
	public int checkBet(RouletteBet b) {
		RBets type = b.getBetType();
		String value = b.getBetValue();
		
		switch (type) {
		case Color:
			if (c.equals(value))
				return 1;
			return 0;
		case Range:
			if (r.equals(value))
				return 1;
			return 0;
		case Parity:
			if (p.equals(value))
				return 1;
			return 0;
		case Value:
			if (v == Integer.parseInt(value))
				return 35;
			return 0;
		}
		return 0; // does not reach
	}
}