import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * @author Rand Hasan, Period 11
 * 2/19/2019
 * Assig3.java CHS Assignment 3
 * This program is a driver class that operates the game of Roulette with the help of methods
 * and other classes
*/
public class Assig3
{
    private static RPList rp;
    private static ArrayList<RoulettePlayer> list;
    
    public static void main(String[] args)
    {
        try {
            rp = new RPList("players.txt"); //fills list
            list = rp.getList(); //ArrayList full of RoulettePlayer objects from the file
            Scanner input = new Scanner(System.in);
            String playAgain = "A"; //initialize to pass through while loop
            boolean gameOver = false;
            while(!playAgain.toUpperCase().equals("QUIT"))
            {
                while (gameOver == false)
                {
                    RoulettePlayer player = login();
                    Double debt = player.getDebt();
                    Double cash = player.getMoney();
                    System.out.println(player.getName()+" you may play as many rounds as you like.  If you run out of money");
                    System.out.println("you may borrow up to a total of 500.0 dollars.  Good luck!\n");
                    play(player); //plays game with the RoulettePlayer object
                    if (player.getMoney() == 0.0) { //player ran out of money
                        if (player.getDebt() == 500) //player has borrowed maximum amount of money
                        {
                            gameOver = true;
                        }
                    }
                }
                playAgain = "A";
                gameOver = false;
            }
        }
        catch (NullPointerException e) //player wrote "QUIT" as their id which caused the login class to return null and the entire game ends
        {
            System.out.println("Game Over -- saving players back to file...");
            rp.saveList(); //writes all data to text file
        }
    }
    
    /**
     * Controls every round of the game of Roulette
     * @param player the RoulettePlayer object who either logged in or signed up to play
     * @return a boolean value based on whether the user wants to quit their game
     */
    public static void play(RoulettePlayer player) {
        try {
            Scanner input = new Scanner(System.in);
            double initialMoney = player.getMoney();
            double bet = 0.0;
            while (initialMoney!=0) {
                int rounds = -1; //starts at -1 in case they quit so the number of rounds will be 0
                boolean quit = false;
                double winnings = 0.0;
                double totalMoney = 0.0;
                double storeMoney = player.getMoney(); //stores for printing data at the end of the player's game
                double debt = player.getDebt();
                while (quit == false)
                {
                    totalMoney += player.getMoney();
                    double money = player.getMoney();
                    if (money > 0) //player has money
                    {
                        rounds ++;
                        while (bet==0) //if bet == -1 then user wants to quit and if bet == 0 then user's bet was not within a valid range
                        {
                            bet = getBet(player.getMoney());
                            if (bet == -1.0) //user typed 0 meaning they want to quit
                            {
                                quit = true;
                                break;
                            }
                        }
                        if (bet == -1.0) //officially quits game - mainly here because the if-statement stopped working for some reason
                        {
                            quit = true;
                            break; //quits player's game and allows a new player to play or end the game entirely
                        }
                        else //game continues
                        {
                            winnings = getGuess(bet);
                            player.updateMoney(winnings);
                        }
                        System.out.println();
                        if (debt!=0 && bet!=-1.0) //user has debt and is still playing
                        {
                            System.out.println("You have some debt that you may wish to pay off.");
                            System.out.println("\tYou owe: "+player.getDebt());
                            System.out.println("\tYou have: "+player.getMoney());
                            double payBack = player.getDebt()+1;
                            while (payBack > player.getDebt()) {
                                System.out.print("\tHow much would you like to pay back (<= "+player.getDebt()+"): ");
                                payBack = Double.parseDouble(input.next());
                            }
                            System.out.println("Great -  you are paying back "+payBack+" of your debt\n");
                        }
                        bet = 0;
                    }
                    else //player has no more money
                    {
                        debt = player.getDebt();
                        System.out.println("Sorry you are out of money.");
                        if (debt>=500) //player has borrowed max amount of money
                        {
                            System.out.println("Unfortunately, you cannot borrow any more money.\n");
                            quit = true;
                            break;
                        }
                        else //player has not borrowed max amount of money
                        {
                            System.out.println("However you may borrow some if you wish");
                            double newMoney = getMoneyToBorrow(storeMoney);
                            player.updateMoney(newMoney);
                            player.updateDebt(newMoney);
                            rounds ++;
                        }
                    }
                }
                double money = player.getMoney();
                debt = player.getDebt();
                System.out.println("Thanks for playing "+player.getName()+" -- come again soon!"); //prints data after round
                System.out.println("Here are your results: ");
                System.out.println("\tRounds played: "+rounds);
                System.out.println("\tStarting money: "+initialMoney);
                System.out.println("\tEnding money: "+player.getMoney());
                System.out.println("\tDebt to house: "+debt);
                if (initialMoney > player.getMoney())
                    System.out.println("\tNet change: "+(money-totalMoney));
                else if (initialMoney < player.getMoney())
                    System.out.println("\tWinnings: "+(money-totalMoney));
                else
                    System.out.println("\tYou broke even!");
                System.out.println();
            }
        }
        catch (NullPointerException e)
        {
            System.out.println("Game Over -- saving players back to file...");
            rp.saveList();
        }
    }
    
    /**
     * Asks user how much money they'd like to borrow
     * @param m the amount of money the player has as they can't borrow more money than they have
     * @return the amount of money the player borrows
     */
    public static double getMoneyToBorrow(double m)
    {
        Scanner input = new Scanner(System.in);
        double borrow = -1.0; //initializes
        try {
            while(borrow < 0 || borrow > m)
            {
                System.out.println("Enter the amount you would like to borrow (<= "+m+")");
                borrow = Double.parseDouble(input.nextLine());
            }
            System.out.println("You have added "+m+" to your account.\n");
            return borrow;
        }
        catch (InputMismatchException e)
        {
            System.out.println("Error: Please enter a valid number");
            getMoneyToBorrow(m);
        }
        return borrow;
    }
    
    /**
     * Gets the user's choice for what they want to bet and returns how much money they win
     * @param betAmount the amount of money the player initially bets
     * @return the amount of money the user wins
     */
    public static double getGuess(double betAmount)
    {
        Scanner input = new Scanner(System.in);
        boolean chosen = false; //initializes boolean value that describes whether the user has chosen the type of their bet
        boolean chosen1 = false; //initializes boolean value that describes whether the user has specifically chosen a value of the type they chose
        String type = "";
        String strChoice = "";
        int intChoice = 0;
        
        while (chosen == false) {
            System.out.println("Please enter the type of your bet: [Value, Color, Range, Parity]");
            type = input.nextLine();
            for (int i = 0; i<4; i++)
            {
                if ((type.toUpperCase().equals("VALUE") && i == 0 ) || (type.toUpperCase().equals("COLOR") && i == 1) || (type.toUpperCase().equals("RANGE") && i == 2) || (type.toUpperCase().equals("PARITY") && i == 3))
                {
                    chosen = true;
                    System.out.println("Your bet type is "+type);
                    while (chosen1 == false)
                    {
                        if (i == 0) //type of bet is value
                        {
                            System.out.print("Enter your number [0-36]: ");
                            intChoice = input.nextInt();
                            if (intChoice>-1 && intChoice<37)
                                chosen1 = true;
                        }
                        else if (i == 1) //type of bet is color
                        {
                            System.out.print("Enter your color [Red, Black]: ");
                            strChoice = input.nextLine();
                            if (strChoice.toUpperCase().equals("RED") || strChoice.toUpperCase().equals("BLACK"))
                                chosen1 = true;
                        }
                        else if (i == 2) //type of bet is range
                        {
                            System.out.print("Enter your range [Low, High]: ");
                            strChoice = input.nextLine();
                            if (strChoice.toUpperCase().equals("LOW") || strChoice.toUpperCase().equals("HIGH"))
                                chosen1 = true;
                        }
                        else //type of bet is parity
                        {
                            System.out.print("Enter your range [Even, Odd]: ");
                            strChoice = input.nextLine();
                            if (strChoice.toUpperCase().equals("EVEN") || strChoice.toUpperCase().equals("ODD"))
                                chosen1 = true;
                        }   
                    }
                }
            }
        }
        RouletteWheel wheel = new RouletteWheel(); //initializes wheel
        RouletteResult X = wheel.spinWheel(); //spins wheel
        RouletteBet bet1 = new RouletteBet(null, strChoice);
        System.out.println("Spin result: " + X);
        if (type.toUpperCase().equals("VALUE"))
        {
            bet1 = new RouletteBet(RBets.Value, Integer.toString(intChoice));
        }
        else if (type.toUpperCase().equals("COLOR"))
        {
            bet1 = new RouletteBet(RBets.Color, strChoice.substring(0, 1).toUpperCase()+ strChoice.substring(1));
        }
        else if (type.toUpperCase().equals("RANGE"))
        {
            bet1 = new RouletteBet(RBets.Range, strChoice.substring(0, 1).toUpperCase()+ strChoice.substring(1));
        }
        else if (type.toUpperCase().equals("PARITY"))
        {
            bet1 = new RouletteBet(RBets.Parity, strChoice.substring(0, 1).toUpperCase()+ strChoice.substring(1));
        }
        double winnings = testBet(wheel, bet1, betAmount); //gets winnings
        return winnings;
    }
    
    /**
     * Returns how much money the user wins based on their bet and what actually happens
     * @param w the wheel
     * @param b the type of bet the player makes
     * @param userB the player's bet
     * @return how much money the user wins
     */
    public static double testBet(RouletteWheel w, RouletteBet b, double userB)
    {
        System.out.println("Bet: " + b);
        int res = w.checkBet(b);
        double winnings = 0;
        if (res == 0) //player loses
        {
            winnings = -1*userB;
            System.out.println("Sorry but you lose your bet of "+userB);
        }
            
        else if (res == 1)
        {
            winnings = userB;
            System.out.println("Even money winner gets "+winnings);
        }
            
        else if (res == 35) //player guesses value
        {
            winnings = 35*userB;
            System.out.println("Big winner! You get "+winnings);
        }
        return winnings;
    }
    
    /**
     * Get the amount of money the user wishes to bet
     * @param pMoney the amount of money the user has
     * @return the bet if valid or other numbers that will cause other actions to perform
     */
    public static double getBet(double pMoney)
    {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("How much do you want to bet? (<="+pMoney+")");
            System.out.print("(Enter 0 if you want to quit): ");
            double bet = Double.parseDouble(input.nextLine());
            if (bet == 0)
                return -1; //user wishes to quit
            else if (bet>0 && bet<=pMoney)
                return bet;
            return 0; //this method will be called again
        }
        catch (InputMismatchException e)
        {
            return 0;
        }
    }
    
    /**
     * Initializes a RoulettePlayer object
     * @return a RoulettePlayer object with a name typed by the user and money either typed by the user or from a previous game
     */
    public static RoulettePlayer getPlayer()
    {
        try {
            Scanner input = new Scanner(System.in);
            String name = "";
            while (name.length() == 0)
            {
                System.out.print("Please enter your name: ");
                name = input.nextLine();
            }
            File f = new File(name+".txt");
            if (f.exists())
            {
                double money = tryFile(name); //will get money the user ended with in the last game they played
                RoulettePlayer player = new RoulettePlayer(name, money);
                return player;
            }
            else //file does not exist so new user
            {
                try {
                    double money = 0.0;
                    FileWriter file = new FileWriter(name+".txt",true);
                    file.write(name+": \n");
                    System.out.println("Welcome "+name);
                    while (money==0.0)
                    {
                        System.out.print("Please enter an amount of money: ");
                        money = input.nextDouble();
                    }
                    file.write(Double.toString(money)+"\n");
                    file.close();
                    RoulettePlayer player = new RoulettePlayer(name, money);
                    return player;
                }
                catch (IOException e)
                {
                    getPlayer();
                }
            }
        }
        catch (InputMismatchException e)
        {
            getPlayer();
        }
        return null;
    }
    
    /**
     * Gets the amount of money a user had in the last game they played
     * @param n the name of the user
     * @return the amount of money the user ended with in the last game they played
     */
    private static double tryFile(String n)
    {
        try {
            File f = new File(n+".txt");
            FileWriter file = new FileWriter(n+".txt",true);    
            Scanner chop = new Scanner(f);
            BufferedReader r = new BufferedReader(new FileReader(n+".txt"));    
            System.out.println("Welcome Back "+n);
            String line = chop.nextLine().trim();
            String st;
            while ((st = r.readLine()) != null) //goes to last line
            {
                line = st;
            }
            file.write(n+":\n");
            double money = Double.parseDouble(line);
            file.write(Double.toString(money)+"\n");
            file.close();
            return money;
        }
        catch (IOException e)
        {
            tryFile(n);
        }
        return 0.0;
    }
    
    /**
     * Returns a boolean based on whether the entered name and password correlate with the name and password of a player in the ArrayList
     * @param n the player's name
     * @param p the player's password
     * @return a boolean value if the player is in the list
     */
    public static boolean loggedIn(String n, String p)
    {
        boolean loggedIn = false;
        try {
            
            for (int j = 0; j<list.size(); j++)
            {
                if (rp.getPlayerPassword(n, p).equals(list.get(j)))
                {
                    loggedIn = true;
                    return loggedIn;
                }
            }
            return loggedIn;
        }
        catch (NullPointerException e)
        {
            return loggedIn;
        }
    }
    
    /**
     * Returns the player with a ame and password that correlate with the name and password that are sent into the method
     * @param n the name of a player
     * @param p the password of a player
     * @return the player with that name and password
     */
    public static RoulettePlayer getPlayer(String n, String p)
    {
        for (int j = 0; j<list.size(); j++)
        {
            if (rp.getPlayerPassword(n, p).equals(list.get(j)))
            {
                return list.get(j);
            }
        }
        return null;
    }
    
    /**
     * Attemps to login to a player's account with their security questions
     * @param n the name of a player
     * @return the player's new password
     */
    public static String getQ(String n)
    {
        Scanner input = new Scanner(System.in);
        String p = "";
        for (RoulettePlayer player:list)
        {
            if (player.getName().equals(n) && (player.hasQuestions())) //if player has the same name as a player in the ArrayList and that player has questions
            {
                String[] q = player.getQuestions();
                System.out.println(q[0]);
                System.out.print("Answer: ");
                String ans1 = input.nextLine();
                System.out.println(q[1]);
                System.out.print("Answer: ");
                String ans2 = input.nextLine();
                System.out.println();
                Question[] questions = player.getQuestionsAndAnswers();
                if (ans1.equals(questions[0].getA()) && ans2.equals(questions[1].getA())) //player enters correct answers
                {
                    System.out.println("We have recovered your account\nPlease reset your password");
                    boolean confirmPass = false;
                    while (confirmPass == false)
                    {
                        System.out.print("New password: ");
                        p = input.nextLine();
                        System.out.print("Repeat new password: ");
                        String p1 = input.nextLine();
                        if (p.equals(p1))
                        {
                            confirmPass = true;
                            System.out.println("Password updated. You are now ready to play\n"); //updates password
                        }
                    }
                    player.setPassword(p); //changes the players password
                    return p; //returns new password
                }
                else {
                    System.out.println("We failed to recover your account. You will have to make a new account.\n"); 
                }
                return p;
            }
        }
        System.out.println("Sorry but your account does not have any security questions.");
        System.out.println("We are not able to recover your records.\n\nIf you want to play you will have to sign in as a new player.\n");
        return "";
    }
    
    /**
     * Creates a new player that failed or chose not to log in
     * @return the new player
     */
    public static RoulettePlayer createNewPlayer() 
    {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Welcome new Roulette Player!\nPlease enter your information below:");
            String name1 = "";
            boolean validName = false;
            while (validName == false) //gets name of new player
            {
                validName = true;
                System.out.print("Name: ");
                name1 = input.nextLine();
                for (int j = 0; j<list.size(); j++)
                {
                    if (list.get(j).getName().equals(name1))
                    {
                        validName = false;
                        System.out.println("Sorry but this id is already used.  Please try again");
                    }
                }
            }
            String pass1 = "1"; //initialization
            String pass2 = "2";
            while (!pass1.equals(pass2)) //gets password of new player and makes player confirm it
            {
                System.out.print("Password: ");
                pass1 = input.nextLine();
                System.out.print("Re-type password: ");
                pass2 = input.nextLine();
                if (!pass1.equals(pass2))
                {
                    System.out.println("Sorry but your passwords do not match");
                }
            }
            System.out.print("Initial Money: "); //gets initial amount of money the player has
            double cash = input.nextDouble();
            System.out.println("You may add two security questions to your account.");
            System.out.println("These can be used to retrieve your profile if you forget your password.");
            String choice = "A";
            while (!(choice.toUpperCase().equals("Y") || choice.toUpperCase().equals("N")))
            {
                System.out.print("Do you wish to add these questions? (Y/N): ");
                choice = input.next();
            }
            input.nextLine();
            if (choice.toUpperCase().equals("Y")) //user enters security questions and their answer
            {
                System.out.print("Question 1: ");
                String q1 = input.nextLine();
                System.out.print("Answer 1: ");
                String a1 = input.nextLine();
                System.out.print("Question 2: ");
                String q2 = input.nextLine();
                System.out.print("Answer 2: ");
                String a2 = input.nextLine();
                Question ques1 = new Question(q1,a1);
                Question ques2 = new Question(q2,a2);
                Question[] questions = {ques1,ques2};
                RoulettePlayer player = new RoulettePlayer(name1, pass1, cash, 0.0,questions);
                System.out.println("Remember your answers -- they must be entered exactly as written");
                System.out.println("here to allow retrieval of your information.\n");
                rp.add(player); //adds player to ArrayList
                return player;
            }
            else
            {
                RoulettePlayer player = new RoulettePlayer(name1, pass1, cash, 0.0); //creates RoulettePlayer object without questions
                rp.add(player);
                return player;
            }
        }
        catch (InputMismatchException e) { //user enters invalid input
            System.out.println("Error: Please try again");
            createNewPlayer();
        }
        return null; //doesn't reach
    }
    
    /**
     * Allows user to attempt to login or sign up to play the game
     * @return the player who gets in the game
     */
    public static RoulettePlayer login()
    {
        RoulettePlayer player = new RoulettePlayer();
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Online Roulette!");
        System.out.println("Please sign in with your id and password");
        System.out.println("If you are a new player, leave your id field blank");
        System.out.println("and we will set you up with a new account");
        System.out.println("If you would like to end the game, enter 'QUIT'\n");
        System.out.print("\tId: ");
        String name = input.nextLine();
        if (name.toUpperCase().equals("QUIT"))
        {
            return null; //will cause an error in main which is caught in the try-catch statements
        }
        int count = 0;
        for (int j = 0; j<list.size(); j++)
        {
            if (list.get(j).getName().equals(name))
            {
                count ++;
            }
        }
        if (count == 0) //player with the entered name isn't already in the list
        {
            player = createNewPlayer(); //makes player make an account
            return player;
        }
        System.out.print("\tPlease enter your password: ");
        String pass = input.nextLine();
        boolean loggedIn = loggedIn(name,pass); //checks if the name and password are the same as one of a player in the ArrayList
        if (loggedIn == false) //such a player does not exist
        {
            System.out.println("\tSorry, your password does not match.  Please try again.");
            System.out.print("\tPlease enter your password: "); //second chance to enter password
            pass = input.nextLine();
            System.out.println();
        }
        loggedIn = loggedIn(name,pass); //tries to check again
        if (loggedIn == true)
        {
            System.out.println();
            player = getPlayer(name, pass); //gets the player with that name and password
        }
        else //fails again
        {
            System.out.println("Your password still does not match, so your sign-in has been");
            System.out.println("canceled.  You still may be able to play if you set security");
            System.out.println("questions.  Otherwise, you will need to register as a new player.\n");
            pass = getQ(name); //attempts to login with security questions
            if (pass.equals("")) //security questions fail so user has to sign up
            {
                player = login();
            }
            else //security questions work so player is looked for in the ArrayList
            {
                player = getPlayer(name, pass);
            }
        }
        return player;
    }
}
