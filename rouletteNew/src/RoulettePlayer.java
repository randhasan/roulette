import java.util.ArrayList;

/**
 * @author Rand Hasan, Period 11
 * 2/19/2019
 * RoulettePlayer.java CHS Assignment 3
 * This program is a class for the game player which includes the player's name and money along with mutator and accessor methods for
 * manipulating and accessing the player's data.
*/
public class RoulettePlayer {
	
	private String name;
	private double money;
	private String password;
	private double debt;
	private Question[] q;

	/**
	 * Initializes a player object
	 */
	public RoulettePlayer() {
		name = "";
	    money = 0.0;
	    password = "";
	    debt = 0.0;
	    q = new Question[2];
	}
	
	/**
	 * Initializes a player object with arguments that are written in the method call as parameters
	 * @param n the player's name
     * @param m the amount of money the player has
	 */
	public RoulettePlayer(String n, double m) //name and money
	{
	    name = "";
	    password = "";
	    money = m;
	    debt = 0.0;
	    q = new Question[2];

	}
	
	/**
	 * Initializes a player object with arguments that are written in the method call as parameters
	 * @param n the player's name
	 * @param p the player's password
	 * @param m the amount of money the player has
	 * @param d the amount of debt the player has
	 */
	public RoulettePlayer(String n, String p, double m, double d) {
		name = n;
		money = m;
		password = p;
		debt = d;
		q = new Question[2];
	}
	
	/**
	 * Initializes a player object with arguments that are written in the method call as parameters
	 * @param n the player's name
	 * @param p the player's password
	 */
	public RoulettePlayer(String n, String p) {
		name = n;
		password = p;
		money = 0.0;
		debt = 0.0;
		q = new Question[2];
	}
	
	/**
	 * Initializes a player object with arguments that are written in the method call as parameters
	 * @param n the player's name
     * @param p the player's password
     * @param m the amount of money the player has
     * @param d the amount of debt the player has
	 * @param ques an array of the player's questions and answers
	 */
	public RoulettePlayer(String n, String p, double m, double d, Question[] ques) {
        name = n;
        money = m;
        password = p;
        debt = d;
        q = ques;
    }
	
	/**
	 * Updates the amount of money the player has by amount delta
	 * @param delta amount of money the player gains or loses
	 */
	public void updateMoney(double delta)
	{
		money += delta;
	}
	
	public void updateDebt(double delta)
	{
	    debt += delta;
	}
	
	/**
	 * Accessor method for player's money
	 * @return the amount of money the player has
	 */
	public double getMoney()
	{
		return money;
	}
	
	/**
	 * Accessor method for the player's name
	 * @return the player's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Accessor method for the player's debt
	 * @return
	 */
	public double getDebt()
	{
	    return debt;
	}
	
	/**
	 * Formats player's information
	 * @return the player's information in a nicely formattted String
	 */
	public String toString()
	{
		return "Name:"+name+" Cash:"+money+" Debt:"+debt;
	}
	
	/**
	 * Checks if player has money
	 * @return a boolean value based on whether the user has money or not
	 */
	
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * Modifier method that sets the password equal to the argument sent into the method
	 * @param p a String that the user's password with become
	 */
	public void setPassword(String p)
	{
		password = p;
	}
	
	/**
	 * Returns a boolean value based on whether the user has any more money
	 * @return a boolean value depending on whether or not the the user has any money left
	 */
	public boolean hasMoney()
	{
		if (money>0)
			return true;
		return false;
	}	
	
	/**
	 * Allows user to borrow money which increase the amount of money and debt they have
	 * @param m the amount of money the user wants to borrow
	 */
	public void borrow(double m)
	{
		debt += m;
		money += m;
	}
	
	/**
	 * Allows the user to pay back their debt which decreases their amount of money and debt
	 * @param m the amount of money the user wants to pay back
	 */
	public void payBack(double m)
	{
		if (!(m>debt) && !(m>money))
		{
			debt -= m;
			money -= m;
		}
		else if (m>debt)
		{
			System.out.println("Amount: "+m+" is more than borrowed: "+debt);
			System.out.println("Only paying back: "+debt);
			money -= (m-debt);
			debt = 0;
		}
		else if (m>money)
		{
			System.out.println("Amount: "+m+" is more than cash: "+money);
			System.out.println("Only paying back: "+money);
			debt -= (m-money);
			money = 0;
		}
	}
	
	/**
	 * Allows user to add an Array of questions to their security question
	 * @param quest an Array that contains the questions the user wishes to become their security questions
	 */
	public void addQuestions(Question[] quest) 
	{
		q[0] = quest[0];
		q[1] = quest[1];
	}
	
	/**
	 * Compares two players passwords and names to see if the players are the same
	 * @param p1 one player
	 * @param p2 another player
	 * @return a boolean value that expresses whether the players are same or not
	 */
	public boolean testEquals(RoulettePlayer p1, RoulettePlayer p2)
	{
		if (p1.getName().equals(p2.getName()) && p1.getPassword().equals(p2.getPassword())) 
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Accessor methods for the users questions
	 * @return an Array containing the users questions
	 */
	public String[] getQuestions()
	{
		String[] questions = new String[2];
		try {
			questions[0] = q[0].getQ();
			questions[1] = q[1].getQ();
			return questions;
		}
		catch (NullPointerException e){ //player does not have any questions
		    questions[0] = "hi"; //makes sure values are not null to prevent errors
            questions[1] = "hi";
            return questions;
		}
	}
	
	/**
	 * Accessor methods for the users questions and answers 
	 * @return an Array containing the users questions and answers
	 */
	public Question[] getQuestionsAndAnswers()
	{
	    Question[] questions = new Question[2];
	    try {
            questions[0] = q[0];
            questions[1] = q[1];
            return questions;
        }
        catch (NullPointerException e){
            return questions;
        }
	}
	
	/**
	 * Formats a player's information in a String
	 * @return a String with a player's information
	 */
	public String saveString() {
		String[] questions = getQuestions();
		return name +","+password+","+money+","+debt+","+questions[0]+","+q[0].getA()+","+questions[1]+","+q[1].getA();
	}
	
	/**
	 * Print's out all of a player's data
	 */
	public void showAllData()
	{
		System.out.println("Name: "+name);
		System.out.println("Password: "+password);
		System.out.println("Cash: "+money);
		System.out.println("Debt: "+debt);
		String[] questions = getQuestions();
		if (questions[0].equals("hi")) //user has no questions
		{
		    System.out.println("Questions: None");
		}
		else //user has two questions
		{
		    System.out.println("Q: "+questions[0]+" A: "+q[0].getA());
            System.out.println("Q: "+questions[1]+" A: "+q[1].getA());
		}
	}
	
	/**
	 * Compares two players to test if they are equal based on their name and password
	 * @param p a player that will be compared to another player
	 * @return a boolean value based on if the two players are equal or not
	 */
	public boolean equals(RoulettePlayer p)
	{
	    if (password.equals(p.getPassword()) && name.equals(p.getName()))
	        return true;
	    return false;
	}
	
	
	/**
	 * Checks if a player's questions and answers match a set of questions and answers that are sent into the method as an Array
	 * @param quest an Array of questions and answers
	 * @return a boolean value based on if the two sets of questions and answers match
	 */
	public boolean matchQuestions(Question[] quest)
	{
		if (quest.length == q.length)
		{
			for (int i = 0; i<quest.length; i++)
			{
				if (!(quest[i].getQ().equals(q[i].getQ())) || !(quest[i].getA().equals(q[i].getA())))
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a formatted String with a user's information that will be written in a text file
	 * @return a formatted String
	 */
	public String format()
    {
        try {
            Question[] q = getQuestionsAndAnswers();
            return name+","+password+","+money+","+debt+","+q[0].getQ()+","+q[0].getA()+","+q[1].getQ()+","+q[1].getA()+"\n";
        }
        catch (NullPointerException e)
        {
            return name+","+password+","+money+","+debt+"\n";
        }
    }
	
	public boolean hasQuestions()
	{
	    String[] questions = getQuestions();
	    if (questions[0].equals("hi"))
	    {
	        return false;
	    }
	    return true;
	}
	
}